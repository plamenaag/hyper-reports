package hyper.reports.repository.error;

public class RepoException extends Exception {
    private String internalMessage;

    private Exception lowLevelException;

    public RepoException(String message){
       this(message,null);
    }

    public RepoException(String message, Exception lowLevelException){
        super(message);
        this.internalMessage = message;
        this.lowLevelException = lowLevelException;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
    }

    public Exception getLowLevelException() {
        return lowLevelException;
    }

    public void setLowLevelException(Exception lowLevelException) {
        this.lowLevelException = lowLevelException;
    }



}
