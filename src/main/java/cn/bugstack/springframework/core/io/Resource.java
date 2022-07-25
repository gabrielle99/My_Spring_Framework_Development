package cn.bugstack.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 处理资源加载流
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
}
