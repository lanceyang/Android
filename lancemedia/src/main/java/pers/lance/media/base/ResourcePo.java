package pers.lance.media.base;

import java.io.Serializable;

/**
 * 资源po
 */
public class ResourcePo implements Serializable {

    /**
     * @see ResourceType
     */
    private ResourceType type;

    private String content;

    private String name;

    private String localPath;

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
