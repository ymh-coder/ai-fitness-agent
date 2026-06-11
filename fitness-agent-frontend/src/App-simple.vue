<template>
  <div class="chat-container">
    <div class="chat-header">
      <h1>AI健身大师</h1>
      <p>您的专属健康管家</p>
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

    // 发送消息 - 使用普通HTTP请求测试
    const sendMessage = async () => {
      if (!inputMessage.value.trim() || isLoading.value) return

      const userMessage = inputMessage.value.trim()
      inputMessage.value = ''
      
      // 添加用户消息
      addMessage('user', userMessage)
      
      // 显示AI正在思考
      isTyping.value = true
      isLoading.value = true

      try {
        console.log('发送消息到后端:', userMessage)
        
        // 先测试普通HTTP请求
        const response = await axios.get('http://localhost:8123/api/ai/manus/chat', {
          params: {
            message: userMessage
          },
          timeout: 30000
        })
        
        console.log('后端响应:', response)
        
        // 模拟AI回复
        isTyping.value = false
        isLoading.value = false
        
        if (response.data) {
          addMessage('ai', `后端响应: ${JSON.stringify(response.data)}`)
        } else {
          addMessage('ai', '收到后端响应，但数据为空')
        }
        
      } catch (error) {
        console.error('请求失败:', error)
        isTyping.value = false
        isLoading.value = false
        
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
      sendMessage
    }
  }
}
</script>

