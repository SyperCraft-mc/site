package fr.kainovaii.sypercraft.app.domain.report;

import fr.kainovaii.sypercraft.app.domain.user.UserDTO;

public class ReportDTO
{
    private final int id;
    private final int messageId;
    private final String message;
    private final long messageDate;
    private final long reportDate;
    private final String server;
    private final String status;
    private final UserDTO author;
    private final UserDTO reporter;

    private ReportDTO(Builder builder) {
        this.id          = builder.id;
        this.messageId   = builder.messageId;
        this.message     = builder.message;
        this.messageDate = builder.messageDate;
        this.reportDate  = builder.reportDate;
        this.server      = builder.server;
        this.status      = builder.status;
        this.author      = builder.author;
        this.reporter    = builder.reporter;
    }

    public static ReportDTO from(Report signalement, UserDTO author, UserDTO reporter) {
        return new Builder()
            .id((Integer) signalement.getId())
            .messageId(signalement.getMessageId())
            .message(signalement.getMessage())
            .messageDate(signalement.getMessageDate())
            .reportDate(signalement.getReportDate())
            .server(signalement.getServer())
            .status(signalement.getStatus())
            .author(author)
            .reporter(reporter)
            .build();
    }

    public int getId()           { return id; }
    public int getMessageId()    { return messageId; }
    public String getMessage()   { return message; }
    public long getMessageDate() { return messageDate; }
    public long getReportDate()  { return reportDate; }
    public String getServer()    { return server; }
    public String getStatus()    { return status; }
    public UserDTO getAuthor()   { return author; }
    public UserDTO getReporter() { return reporter; }

    public boolean isPending() {
        return Report.STATUS_PENDING.equals(status);
    }

    public boolean isProcessing() {
        return Report.STATUS_PROCESSING.equals(status);
    }

    public static class Builder {
        private int id;
        private int messageId;
        private String message;
        private long messageDate;
        private long reportDate;
        private String server;
        private String status;
        private UserDTO author;
        private UserDTO reporter;

        public Builder id(int id)                    { this.id = id; return this; }
        public Builder messageId(int messageId)      { this.messageId = messageId; return this; }
        public Builder message(String message)       { this.message = message; return this; }
        public Builder messageDate(long messageDate) { this.messageDate = messageDate; return this; }
        public Builder reportDate(long reportDate)   { this.reportDate = reportDate; return this; }
        public Builder server(String server)         { this.server = server; return this; }
        public Builder status(String status)         { this.status = status; return this; }
        public Builder author(UserDTO author)        { this.author = author; return this; }
        public Builder reporter(UserDTO reporter)    { this.reporter = reporter; return this; }

        public ReportDTO build() { return new ReportDTO(this); }
    }
}