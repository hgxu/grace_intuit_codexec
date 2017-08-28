package com.grace.expense.controller;

import com.grace.expense.model.Invoice;
import com.grace.expense.model.LineItem;
import com.grace.expense.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by Grace Xu on 8/23/17.
 */
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Invoice addInvoice(@Validated @RequestBody Invoice invoice) {
        final Invoice model = new Invoice();

        model.setName(invoice.getName());
        model.setEmail(invoice.getEmail());
        model.setDueDate(invoice.getDueDate());

        invoice.getLineItems()
                .stream()
                .map(lineItem -> {
                    final LineItem clone = new LineItem();
                    clone.setDescription(lineItem.getDescription());
                    clone.setAmount(new BigDecimal(lineItem.getAmount().toString()));
                    return clone;
                })
                .forEach(lineItem -> model.addLineItem(lineItem));

        invoiceRepository.saveAndFlush(model);

        return model;
    }
}
