package fr.kainovaii.sypercraft.app.domain.report;

import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository
{
    public List<Report> findAll() {
        return Report.where("1=1").orderBy("date_signalement ASC").load();
    }

    public Optional<Report> findById(int id) {
        return Optional.ofNullable(Report.findById(id));
    }

    public List<Report> findByStatus(String status) {
        return Report.where("statut = ?", status).load();
    }

    public List<Report> findPending() {
        return findByStatus(Report.STATUS_PENDING);
    }

    public List<Report> findProcessing() {
        return findByStatus(Report.STATUS_PROCESSING);
    }

    public List<Report> findByAuthorUuid(String authorUuid) {
        return Report.where("uuid_auteur = ?", authorUuid).load();
    }

    public List<Report> findByReporterUuid(String reporterUuid) {
        return Report.where("uuid_signaleur = ?", reporterUuid).load();
    }

    public List<Report> findByServer(String server) {
        return Report.where("serveur = ?", server).load();
    }

    public Optional<Report> findByMessageId(int messageId) {
        return Optional.ofNullable(
                Report.findFirst("message_id = ?", messageId)
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
        report
                .setMessageId(messageId)
                .setAuthorUuid(authorUuid)
                .setMessage(message)
                .setMessageDate(messageDate)
                .setReporterUuid(reporterUuid)
                .setReportDate(System.currentTimeMillis())
                .setServer(server)
                .setStatus(Report.STATUS_PENDING);

        report.saveIt();
        return report;
    }

    public boolean markAsProcessing(int id) {
        return findById(id)
                .map(report -> {
                    report.markAsProcessing();
                    return report.saveIt();
                })
                .orElse(false);
    }

    public boolean updateStatus(int id, String status) {
        return findById(id)
                .map(report -> {
                    report.setStatus(status);
                    return report.saveIt();
                })
                .orElse(false);
    }

    public boolean delete(int id) {
        return findById(id)
                .map(Report::delete)
                .orElse(false);
    }
}