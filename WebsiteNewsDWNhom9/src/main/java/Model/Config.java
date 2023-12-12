package Model;

import java.io.Serializable;

public class Config  implements Serializable {
    private int id;
    private String folderName;
    private String stagingSourceUsername;
    private String stagingSourcePassword;
    private int stagingSourcePort;
    private String stagingDbName;
    private String stagingSourceHost;
    private String whSourceUsername;
    private String whSourcePassword;
    private int whSourcePort;
    private String whDbName;
    private String whSourceHost;
    private String martSourceUsername;
    private String martSourcePassword;
    private int martSourcePort;
    private String martDbName;
    private String martSourceHost;
    private String downloadPath;
    private String fileColumnList;
    private String fileVariable;
    private String delimiter;
    private String errorToMail;
    private String flag;
    private String status;

    private String webUrl;

    // Constructors, getters, and setters

    // Constructor


    public Config(int id, String folderName, String stagingSourceUsername, String stagingSourcePassword, int stagingSourcePort, String stagingDbName, String stagingSourceHost, String whSourceUsername, String whSourcePassword, int whSourcePort, String whDbName, String whSourceHost, String martSourceUsername, String martSourcePassword, int martSourcePort, String martDbName, String martSourceHost, String downloadPath, String fileColumnList, String fileVariable, String delimiter, String errorToMail, String flag, String status, String webUrl) {
        this.id = id;
        this.folderName = folderName;
        this.stagingSourceUsername = stagingSourceUsername;
        this.stagingSourcePassword = stagingSourcePassword;
        this.stagingSourcePort = stagingSourcePort;
        this.stagingDbName = stagingDbName;
        this.stagingSourceHost = stagingSourceHost;
        this.whSourceUsername = whSourceUsername;
        this.whSourcePassword = whSourcePassword;
        this.whSourcePort = whSourcePort;
        this.whDbName = whDbName;
        this.whSourceHost = whSourceHost;
        this.martSourceUsername = martSourceUsername;
        this.martSourcePassword = martSourcePassword;
        this.martSourcePort = martSourcePort;
        this.martDbName = martDbName;
        this.martSourceHost = martSourceHost;
        this.downloadPath = downloadPath;
        this.fileColumnList = fileColumnList;
        this.fileVariable = fileVariable;
        this.delimiter = delimiter;
        this.errorToMail = errorToMail;
        this.flag = flag;
        this.status = status;
        this.webUrl = webUrl;
    }

    public Config() {

    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", folderName='" + folderName + '\'' +
                ", stagingSourceUsername='" + stagingSourceUsername + '\'' +
                ", stagingSourcePassword='" + stagingSourcePassword + '\'' +
                ", stagingSourcePort=" + stagingSourcePort +
                ", stagingDbName='" + stagingDbName + '\'' +
                ", stagingSourceHost='" + stagingSourceHost + '\'' +
                ", whSourceUsername='" + whSourceUsername + '\'' +
                ", whSourcePassword='" + whSourcePassword + '\'' +
                ", whSourcePort=" + whSourcePort +
                ", whDbName='" + whDbName + '\'' +
                ", whSourceHost='" + whSourceHost + '\'' +
                ", martSourceUsername='" + martSourceUsername + '\'' +
                ", martSourcePassword='" + martSourcePassword + '\'' +
                ", martSourcePort=" + martSourcePort +
                ", martDbName='" + martDbName + '\'' +
                ", martSourceHost='" + martSourceHost + '\'' +
                ", downloadPath='" + downloadPath + '\'' +
                ", fileColumnList='" + fileColumnList + '\'' +
                ", fileVariable='" + fileVariable + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", errorToMail='" + errorToMail + '\'' +
                ", flag='" + flag + '\'' +
                ", status='" + status + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getStagingSourceUsername() {
        return stagingSourceUsername;
    }

    public void setStagingSourceUsername(String stagingSourceUsername) {
        this.stagingSourceUsername = stagingSourceUsername;
    }

    public String getStagingSourcePassword() {
        return stagingSourcePassword;
    }

    public void setStagingSourcePassword(String stagingSourcePassword) {
        this.stagingSourcePassword = stagingSourcePassword;
    }

    public int getStagingSourcePort() {
        return stagingSourcePort;
    }

    public void setStagingSourcePort(int stagingSourcePort) {
        this.stagingSourcePort = stagingSourcePort;
    }

    public String getStagingDbName() {
        return stagingDbName;
    }

    public void setStagingDbName(String stagingDbName) {
        this.stagingDbName = stagingDbName;
    }

    public String getStagingSourceHost() {
        return stagingSourceHost;
    }

    public void setStagingSourceHost(String stagingSourceHost) {
        this.stagingSourceHost = stagingSourceHost;
    }

    public String getWhSourceUsername() {
        return whSourceUsername;
    }

    public void setWhSourceUsername(String whSourceUsername) {
        this.whSourceUsername = whSourceUsername;
    }

    public String getWhSourcePassword() {
        return whSourcePassword;
    }

    public void setWhSourcePassword(String whSourcePassword) {
        this.whSourcePassword = whSourcePassword;
    }

    public int getWhSourcePort() {
        return whSourcePort;
    }

    public void setWhSourcePort(int whSourcePort) {
        this.whSourcePort = whSourcePort;
    }

    public String getWhDbName() {
        return whDbName;
    }

    public void setWhDbName(String whDbName) {
        this.whDbName = whDbName;
    }

    public String getWhSourceHost() {
        return whSourceHost;
    }

    public void setWhSourceHost(String whSourceHost) {
        this.whSourceHost = whSourceHost;
    }

    public String getMartSourceUsername() {
        return martSourceUsername;
    }

    public void setMartSourceUsername(String martSourceUsername) {
        this.martSourceUsername = martSourceUsername;
    }

    public String getMartSourcePassword() {
        return martSourcePassword;
    }

    public void setMartSourcePassword(String martSourcePassword) {
        this.martSourcePassword = martSourcePassword;
    }

    public int getMartSourcePort() {
        return martSourcePort;
    }

    public void setMartSourcePort(int martSourcePort) {
        this.martSourcePort = martSourcePort;
    }

    public String getMartDbName() {
        return martDbName;
    }

    public void setMartDbName(String martDbName) {
        this.martDbName = martDbName;
    }

    public String getMartSourceHost() {
        return martSourceHost;
    }

    public void setMartSourceHost(String martSourceHost) {
        this.martSourceHost = martSourceHost;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getFileColumnList() {
        return fileColumnList;
    }

    public void setFileColumnList(String fileColumnList) {
        this.fileColumnList = fileColumnList;
    }

    public String getFileVariable() {
        return fileVariable;
    }

    public void setFileVariable(String fileVariable) {
        this.fileVariable = fileVariable;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getErrorToMail() {
        return errorToMail;
    }

    public void setErrorToMail(String errorToMail) {
        this.errorToMail = errorToMail;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
