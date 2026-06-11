package com.ymh.ymhaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件持久化的 对话记忆实现类
 */
public class FileBasedChatMemory implements ChatMemory {

    private static final Kryo kryo = new Kryo();

    private final String BASE_DIR;

    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public FileBasedChatMemory(String dir) {
        BASE_DIR = dir;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        conversationMessages.addAll(messages);
        saveConversation(conversationId,conversationMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        return getOrCreateConversation(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取会话对应的文件
     *
     * @param conversationId 会话id
     * @return File对象
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }

    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages=new ArrayList<>();
        if(file.exists()) {
            try(Input input=new Input(new FileInputStream(file))){
                messages=kryo.readObject(input,ArrayList.class);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return messages;
    }

    /**
     * 写入 会话内容
     *
     * @param conversationId     文件
     * @param messages 会话内容
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
