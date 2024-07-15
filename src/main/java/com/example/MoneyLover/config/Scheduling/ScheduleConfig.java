package com.example.MoneyLover.config.Scheduling;

import com.example.MoneyLover.config.Mail.MailDto;
import com.example.MoneyLover.config.Mail.MailService;
import com.example.MoneyLover.infra.Bills.Entity.Bill;
import com.example.MoneyLover.infra.Bills.Repository.BillRepo;
import com.example.MoneyLover.infra.Recurring.Entity.Recurring;
import com.example.MoneyLover.infra.Recurring.Repository.RecurringRepo;
import com.example.MoneyLover.infra.Transaction.Dto.TransEmail;
import com.example.MoneyLover.infra.Transaction.Entity.Transaction;
import com.example.MoneyLover.infra.Transaction.Repository.TransactionRepo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleConfig {
    private final Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    private final MailService mailService;
    private final String cron = "0 0 9 * * ?";

    private final TransactionRepo transactionRepo;
    private final BillRepo billRepo;
    private final RecurringRepo recurringRepo;

    @Scheduled(cron=cron)
    public void deleteTransaction() throws MessagingException {
        logger.info("deleteTransaction started");
        LocalDate now = LocalDate.now();
        logger.info("now: " + now);
        processTransactions(now);
        processBills(now);
        logger.info("deleteTransaction completed");
    }

    private void processBills(LocalDate now) throws MessagingException {
        List<Bill> bills = billRepo.findBills();
        for (Bill bill : bills) {
            Recurring reur = recurringRepo.findById(bill.getRecurring().getId()).orElseThrow();
            LocalDate from_date = reur.getFrom_date();
            LocalDate to_date = reur.getTo_date();
            if(((now.isAfter(from_date) || now.isEqual(from_date)) && (to_date==null || (now.isBefore(to_date) || now.isEqual(to_date))))&&!reur.isDone() ) {
                processBillAndSendNoti(bill,reur);
            }
        }
    }

private void processBillAndSendNoti(Bill bill, Recurring reur) throws MessagingException {
    Bill newBill = new Bill(bill);
    newBill.setRecurring(null);
    billRepo.save(newBill);
    plusTime(reur, bill.getRecurring().getFrom_date(), LocalDate.now(), bill.getRecurring().getTo_date());
}

    public void processTransactions(LocalDate now) throws MessagingException {
        List<Transaction> transRecurring = transactionRepo.getTrans();
        for (Transaction trans : transRecurring) {
            Recurring reur = recurringRepo.findById(trans.getRecurring().getId()).orElseThrow();
            LocalDate from_date = reur.getFrom_date();
            LocalDate to_date = reur.getTo_date();

            int dayInWeek = now.getDayOfWeek().getValue();
            int dayInMonth = now.getDayOfMonth();
            if(((now.isAfter(from_date) || now.isEqual(from_date)) && (to_date==null || (now.isBefore(to_date) || now.isEqual(to_date))))&&!reur.isDone() ) {

                if (reur.getFrequency().equals("weeks")) {
                    if (dayInWeek == reur.getDate_of_week() && (now.isEqual(from_date) || now.isAfter(from_date))) {
                        processAndSendEmail(trans, reur);
                    }

                } else if (reur.getFrequency().equals("months")) {
                    if (dayInMonth == reur.getOccurrence_in_month() && (now.isEqual(from_date) || now.isAfter(from_date))) {
                        processAndSendEmail(trans, reur);
                    }
                }else{
                    processAndSendEmail(trans, reur);
                }
            }
        }
    }

    private void processAndSendEmail(Transaction trans, Recurring reur) throws MessagingException {
        Transaction newTran = new Transaction(trans);
        newTran.setRemind(false);
        newTran.setRecurring(null);
        plusTime(reur, trans.getRecurring().getFrom_date(), LocalDate.now(), trans.getRecurring().getTo_date());
        transactionRepo.save(newTran);
//        sendMail(trans);
    }

    private void sendMail(Transaction trans) throws MessagingException {
        TransEmail transEmail = new TransEmail(trans.getUser().getEmail(), trans.getAmount(), trans.getCategory().getName(), trans.getNotes());
        MailDto<TransEmail> mailDto = new MailDto<>(trans.getUser().getEmail(), "Sending transaction " + trans.getCategory().getName(), transEmail, "mail/transaction");
        mailService.sendMailTransaction(mailDto);
    }

    private void plusTime(Recurring reur,LocalDate from_date,LocalDate now,LocalDate to_date){
        if(reur.getFor_time()!=0) {
            reur.setSend_count(reur.getSend_count() + 1);
        }

        if(reur.getFrequency().equals("days")) {
            reur.setFrom_date(from_date.plusDays(reur.getEvery()));
        }

        if(reur.getFrequency().equals("weeks")) {
            reur.setFrom_date(from_date.plusWeeks(reur.getEvery()));
        }

        if(reur.getFrequency().equals("months")) {
            reur.setFrom_date(from_date.plusMonths(reur.getEvery()));
        }

        if(reur.getFrequency().equals("years")) {
            reur.setFrom_date(from_date.plusYears(reur.getEvery()));
        }

        if(!reur.isForever() &&reur.getTo_date()==null &&reur.getFor_time()==reur.getSend_count())
        {
            reur.setDone(true);
        }

        if(to_date != null&&now.isEqual(to_date)){
            reur.setDone(true);
        }
        recurringRepo.save(reur);
    }
}
