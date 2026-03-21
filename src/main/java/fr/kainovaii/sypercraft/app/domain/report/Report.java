package fr.kainovaii.sypercraft.app.domain.report;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.database.orm.model.Table;

@Table("game_signalements")
public class Report extends Model
{

    public static final String STATUS_PENDING    = "EN_ATTENTE";
    public static final String STATUS_PROCESSING = "EN_COURS_TRAITEMENT";

    public int getMessageId() {
        return getInteger("message_id");
    }

    public String getAuthorUuid() {
        return getString("uuid_auteur");
    }

    public String getMessage() {
        return getString("message");
    }

    public long getMessageDate() {
        return getLong("date_message");
    }

    public String getReporterUuid() {
        return getString("uuid_signaleur");
    }

    public long getReportDate() {
        return getLong("date_signalement");
    }

    public String getServer() {
        return getString("serveur");
    }

    public String getStatus() {
        return getString("statut");
    }

    public Report setMessageId(int messageId) {
        set("message_id", messageId);
        return this;
    }

    public Report setAuthorUuid(String authorUuid) {
        set("uuid_auteur", authorUuid);
        return this;
    }

    public Report setMessage(String message) {
        set("message", message);
        return this;
    }

    public Report setMessageDate(long messageDate) {
        set("date_message", messageDate);
        return this;
    }

    public Report setReporterUuid(String reporterUuid) {
        set("uuid_signaleur", reporterUuid);
        return this;
    }

    public Report setReportDate(long reportDate) {
        set("date_signalement", reportDate);
        return this;
    }

    public Report setServer(String server) {
        set("serveur", server);
        return this;
    }

    public Report setStatus(String status) {
        set("statut", status);
        return this;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    public boolean isPending() {
        return STATUS_PENDING.equals(getStatus());
    }

    public boolean isProcessing() {
        return STATUS_PROCESSING.equals(getStatus());
    }

    public Report markAsProcessing() {
        return setStatus(STATUS_PROCESSING);
    }
}