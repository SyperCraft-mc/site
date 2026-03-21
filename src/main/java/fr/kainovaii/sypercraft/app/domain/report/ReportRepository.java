package fr.kainovaii.sypercraft.app.domain.report;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository
{
    public List<Report> findAll() {
        return Model.query(Report.class)
                .orderBy("date_signalement")
                .get();
    }

    public Optional<Report> findById(int id) {
        return Optional.ofNullable(Model.find(Report.class, id));
    }

    public List<Report> findByStatus(String status) {
        return Model.query(Report.class)
                .where("statut", status)
                .get();
    }

    public List<Report> findPending() {
        return findByStatus(Report.STATUS_PENDING);
    }

    public List<Report> findProcessing() {
        return findByStatus(Report.STATUS_PROCESSING);
    }

    public List<Report> findByAuthorUuid(String authorUuid) {
        return Model.query(Report.class)
                .where("uuid_auteur", authorUuid)
                .get();
    }

    public List<Report> findByReporterUuid(String reporterUuid) {
        return Model.query(Report.class)
                .where("uuid_signaleur", reporterUuid)
                .get();
    }

    public List<Report> findByServer(String server) {
        return Model.query(Report.class)
                .where("serveur", server)
                .get();
    }

    public Optional<Report> findByMessageId(int messageId) {
        return Optional.ofNullable(
                Model.query(Report.class)
                        .where("message_id", messageId)
                        .first()
        );
    }

    public Report create(
            int messageId,
            String authorUuid,
            String message,
            long messageDate,
            String reporterUuid,
            String server
    ) {
        Report report = new Report();
        report.set("message_id", messageId)
                .set("uuid_auteur", authorUuid)
                .set("message", message)
                .set("date_message", messageDate)
                .set("uuid_signaleur", reporterUuid)
                .set("date_signalement", System.currentTimeMillis())
                .set("serveur", server)
                .set("statut", Report.STATUS_PENDING);

        report.save();
        return report;
    }

    public boolean markAsProcessing(int id) {
        return findById(id)
                .map(report -> {
                    report.markAsProcessing();
                    return report.save();
                })
                .orElse(false);
    }

    public boolean updateStatus(int id, String status) {
        return findById(id)
                .map(report -> {
                    report.set("statut", status);
                    return report.save();
                })
                .orElse(false);
    }

    public boolean delete(int id) {
        return findById(id)
                .map(Report::delete)
                .orElse(false);
    }
}