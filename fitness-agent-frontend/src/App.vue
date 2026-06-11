<template>
  <div class="chat-container">
    <div class="chat-header">
      <h1>AI健身大师</h1>
      <p>您的专属健康管家</p>
      <div style="margin-top: 10px;">
        <button @click="toggleConnectionMode" style="background: rgba(255,255,255,0.2); border: 1px solid white; color: white; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
          {{ useSSE ? '切换到HTTP' : '切换到SSE' }}
        </button>
        <span style="margin-left: 10px; font-size: 12px;">
          当前模式: {{ useSSE ? 'SSE流式连接' : 'HTTP普通请求' }}
        </span>
        <button @click="toggleTypeWriter" style="background: rgba(255,255,255,0.2); border: 1px solid white; color: white; padding: 5px 10px; border-radius: 5px; cursor: pointer; margin-left: 10px;">
          {{ enableTypeWriter ? '关闭打字机' : '开启打字机' }}
        </button>
      </div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="(message, index) in messages" 
        :key="index"
        :class="['message', message.type]"
      >
        <div class="message-avatar">
          {{ message.type === 'user' ? '我' : 'AI' }}
        </div>
        <div class="message-content">
          {{ message.content }}
        </div>
      </div>
      
      <!-- 打字指示器 -->
      <div v-if="isTyping" class="message ai">
        <div class="message-avatar">AI</div>
        <div class="message-content">
          <div class="typing-indicator">
            AI正在思考
            <div class="typing-dots">
              <div class="typing-dot"></div>
              <div class="typing-dot"></div>
              <div class="typing-dot"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input-container">
      <input
        v-model="inputMessage"
        @keyup.enter="sendMessage"
        :disabled="isLoading"
        class="chat-input"
        placeholder="请输入您的问题..."
        type="text"
      />
      <button
        @click="sendMessage"
        :disabled="isLoading || !inputMessage.trim()"
        class="send-button"
      >
        {{ isLoading ? '发送中...' : '发送' }}
      </button>
    </div>
    
    <!-- 页脚版权信息 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="#" @click.prevent>关于我们</a>
          <a href="#" @click.prevent>隐私政策</a>
          <a href="#" @click.prevent>服务条款</a>
          <a href="#" @click.prevent>联系我们</a>
        </div>
        <div class="footer-copyright">
          © 2025 AI健身大师. 保留所有权利. | 让AI成为您的专属健康管家
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'

export default {
  name: 'App',
  setup() {
    const messages = ref([])
    const inputMessage = ref('')
    const isLoading = ref(false)
    const isTyping = ref(false)
    const messagesContainer = ref(null)
    const useSSE = ref(true) // 默认使用SSE
    const enableTypeWriter = ref(false) // 默认关闭打字机效果，使用流式显示

    // 添加消息到聊天记录
    const addMessage = (type, content) => {
      messages.value.push({
        type,
        content,
        timestamp: new Date()
      })
      scrollToBottom()
    }

    // 滚动到底部
    const scrollToBottom = () => {
      nextTick(() => {
        if (messagesContainer.value) {
          messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
        }
      })
    }

    // 打字机效果
    const typeWriterEffect = (fullText, messageIndex) => {
      if (messageIndex < 0 || messageIndex >= messages.value.length) return
      
      const currentText = messages.value[messageIndex].content
      const targetText = fullText
      
      // 如果当前文本已经等于目标文本，不需要打字机效果
      if (currentText === targetText) return
      
      // 清除之前的打字机定时器
      if (messages.value[messageIndex].typeWriterTimer) {
        clearInterval(messages.value[messageIndex].typeWriterTimer)
      }
      
      let currentIndex = currentText.length
      
      const typeWriterTimer = setInterval(() => {
        if (currentIndex < targetText.length) {
          currentIndex++
          messages.value[messageIndex].content = targetText.substring(0, currentIndex)
          scrollToBottom()
        } else {
          clearInterval(typeWriterTimer)
          messages.value[messageIndex].typeWriterTimer = null
        }
      }, 20) // 每20ms显示一个字符，更流畅
      
      // 保存定时器引用，用于清理
      messages.value[messageIndex].typeWriterTimer = typeWriterTimer
    }

    // 直接更新消息内容（用于SSE流式数据）
    const updateMessageContent = (content, messageIndex) => {
      if (messageIndex < 0 || messageIndex >= messages.value.length) return
      
      // 清除打字机效果
      if (messages.value[messageIndex].typeWriterTimer) {
        clearInterval(messages.value[messageIndex].typeWriterTimer)
        messages.value[messageIndex].typeWriterTimer = null
      }
      
      // 直接更新内容
      messages.value[messageIndex].content = content
      scrollToBottom()
    }

    // 切换连接模式
    const toggleConnectionMode = () => {
      useSSE.value = !useSSE.value
      addMessage('ai', `已切换到${useSSE.value ? 'SSE流式连接' : 'HTTP普通请求'}模式`)
    }

    // 切换打字机效果
    const toggleTypeWriter = () => {
      enableTypeWriter.value = !enableTypeWriter.value
      addMessage('ai', `打字机效果已${enableTypeWriter.value ? '开启' : '关闭'}`)
    }

    // HTTP请求方式
    const sendHttpMessage = async (userMessage) => {
      try {
        console.log('发送HTTP请求:', userMessage)
        
        const response = await axios.get('http://localhost:8123/api/ai/manus/chat', {
          params: { message: userMessage },
          timeout: 30000
        })
        
        console.log('HTTP响应:', response)
        
        if (response.data) {
          addMessage('ai', `HTTP响应: ${JSON.stringify(response.data)}`)
        } else {
          addMessage('ai', '收到HTTP响应，但数据为空')
        }
        
      } catch (error) {
        console.error('HTTP请求失败:', error)
        
        if (error.code === 'ECONNREFUSED') {
          addMessage('ai', '❌ 无法连接到后端服务，请检查：\n1. 后端服务是否已启动在 http://localhost:8123\n2. 后端服务是否正常运行')
        } else if (error.response) {
          addMessage('ai', `❌ 后端返回错误：${error.response.status} - ${error.response.statusText}`)
        } else if (error.request) {
          addMessage('ai', '❌ 网络请求失败，请检查网络连接')
        } else {
          addMessage('ai', `❌ 请求错误：${error.message}`)
        }
      }
    }

    // SSE连接方式
    const sendSSEMessage = async (userMessage) => {
      try {
        console.log('开始连接SSE:', `http://localhost:8123/api/ai/manus/chat?message=${encodeURIComponent(userMessage)}`)
        
        const eventSource = new EventSource(
          `http://localhost:8123/api/ai/manus/chat?message=${encodeURIComponent(userMessage)}`
        )

        let aiResponse = ''
        let aiMessageIndex = -1
        let hasReceivedData = false

        // 监听连接打开
        eventSource.onopen = (event) => {
          console.log('SSE连接已打开:', event)
        }

        // 监听消息
        eventSource.onmessage = (event) => {
          console.log('收到SSE消息:', event.data)
          hasReceivedData = true
          
          if (event.data === '[DONE]') {
            console.log('SSE流结束')
            eventSource.close()
            isTyping.value = false
            isLoading.value = false
            return
          }

          try {
            // 尝试解析JSON数据
            let data
            try {
              data = JSON.parse(event.data)
            } catch (parseError) {
              // 如果不是JSON，直接使用原始数据
              data = { content: event.data }
            }
            
            if (data.content) {
              // 处理SSE消息内容，去除多余的换行符
              let processedContent = data.content
              
              // 去除多余的换行符（后端每完成一步会响应一次，可能包含多余换行）
              processedContent = processedContent.replace(/\n{2,}/g, '\n').trim()
              
              // 累积完整响应
              aiResponse += processedContent
              
              // 如果是第一条消息，创建AI消息
              if (aiMessageIndex === -1) {
                isTyping.value = false
                messages.value.push({
                  type: 'ai',
                  content: '',
                  timestamp: new Date()
                })
                aiMessageIndex = messages.value.length - 1
              }
              
              // 根据设置选择显示方式
              if (enableTypeWriter.value) {
                typeWriterEffect(aiResponse, aiMessageIndex)
              } else {
                updateMessageContent(aiResponse, aiMessageIndex)
              }
            }
          } catch (error) {
            console.error('处理SSE数据失败:', error)
            // 如果解析失败，直接显示原始数据，同样处理换行符
            let processedData = event.data.replace(/\n{2,}/g, '\n').trim()
            aiResponse += processedData
            
            if (aiMessageIndex === -1) {
              isTyping.value = false
              messages.value.push({
                type: 'ai',
                content: '',
                timestamp: new Date()
              })
              aiMessageIndex = messages.value.length - 1
            }
            
            // 根据设置选择显示方式
            if (enableTypeWriter.value) {
              typeWriterEffect(aiResponse, aiMessageIndex)
            } else {
              updateMessageContent(aiResponse, aiMessageIndex)
            }
          }
        }

        // 监听错误
        eventSource.onerror = (error) => {
          console.error('SSE连接错误:', error)
          console.log('EventSource状态:', eventSource.readyState)
          
          eventSource.close()
          isTyping.value = false
          isLoading.value = false
          
          if (!hasReceivedData) {
            // 如果没有收到任何数据，可能是后端服务未启动或CORS问题
            addMessage('ai', '无法连接到后端服务，请检查：\n1. 后端服务是否已启动在 http://localhost:8123\n2. 后端是否配置了CORS支持\n3. 网络连接是否正常')
          } else {
            addMessage('ai', '连接中断，请重试。')
          }
        }

        // 设置超时
        setTimeout(() => {
          if (eventSource.readyState !== EventSource.CLOSED) {
            console.log('SSE连接超时')
            eventSource.close()
            isTyping.value = false
            isLoading.value = false
            
            if (!hasReceivedData) {
              addMessage('ai', '连接超时，请检查后端服务是否正常运行。')
            }
          }
        }, 30000) // 30秒超时

      } catch (error) {
        console.error('发送消息失败:', error)
        isTyping.value = false
        isLoading.value = false
        addMessage('ai', '发送消息失败，请检查网络连接和后端服务状态。')
      }
    }

    // 发送消息
    const sendMessage = async () => {
      if (!inputMessage.value.trim() || isLoading.value) return

      const userMessage = inputMessage.value.trim()
      inputMessage.value = ''
      
      // 添加用户消息
      addMessage('user', userMessage)
      
      // 显示AI正在思考
      isTyping.value = true
      isLoading.value = true

      if (useSSE.value) {
        await sendSSEMessage(userMessage)
      } else {
        await sendHttpMessage(userMessage)
        isTyping.value = false
        isLoading.value = false
      }
    }

    // 组件挂载时显示AI开场白
    onMounted(() => {
      setTimeout(() => {
        addMessage('ai', '您好！我是您的专属健康管家。在制定方案前，我需要先了解您的身体状况。您方便用30秒描述最近一次运动后的身体感受吗？')
      }, 1000)
    })

    return {
      messages,
      inputMessage,
      isLoading,
      isTyping,
      messagesContainer,
      useSSE,
      enableTypeWriter,
      toggleConnectionMode,
      toggleTypeWriter,
      sendMessage
    }
  }
}
</script>
