package cn.bugstack.springframework.core.io;

import cn.bugstack.springframework.utils.ClassUtils;
import cn.hutool.core.lang.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource{
    private final String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
//        this.path = path;
//        this.classLoader = (ClassLoader) null;
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path,"Path must be not null");
        this.path = path;
        this.classLoader = (classLoader != null? classLoader: ClassUtils.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = this.classLoader.getResourceAsStream(this.path);
        if (inputStream == null){
            throw new FileNotFoundException(this.path+" cannot be opened since it does not exist.");
        }
        return inputStream;
    }
}
