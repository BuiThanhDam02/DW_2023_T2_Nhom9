package Models;

import java.io.Serializable;
import java.sql.Timestamp;

public class ConfigFile implements Serializable {
    private int id;
    private int dfConfigId;
    private String name;
    private String path;
    private String extension;
    private String delimiter;
    private Timestamp fileTimestamp;
    private String note;
    private Timestamp createdAt;
    private String createdBy;
    private int isDelete;
    private String status;

    public ConfigFile(int id, int dfConfigId, String name, String path, String extension, String delimiter, Timestamp fileTimestamp, String note, Timestamp createdAt, String createdBy, int isDelete, String status) {
        this.id = id;
        this.dfConfigId = dfConfigId;
        this.name = name;
        this.path = path;
        this.extension = extension;
        this.delimiter = delimiter;
        this.fileTimestamp = fileTimestamp;
        this.note = note;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.isDelete = isDelete;
        this.status = status;
    }
    public ConfigFile(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDfConfigId() {
        return dfConfigId;
    }

    public void setDfConfigId(int dfConfigId) {
        this.dfConfigId = dfConfigId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Timestamp getFileTimestamp() {
        return fileTimestamp;
    }

    public void setFileTimestamp(Timestamp fileTimestamp) {
        this.fileTimestamp = fileTimestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConfigFile{" +
                "id=" + id +
                ", dfConfigId=" + dfConfigId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", extension='" + extension + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", fileTimestamp=" + fileTimestamp +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", isDelete=" + isDelete +
                ", status='" + status + '\'' +
                '}';
    }
}

