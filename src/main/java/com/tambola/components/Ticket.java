package com.tambola.components;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by shivakumargudimalla on 8/29/19.
 */
public class Ticket {

    private Integer rows;
    private Integer columns;
    private Integer itemsPerRow;
    private Integer totalNumberCount;
    private ThreadLocalRandom threadLocalRandom;
    private List<List<Integer>> ticketNumbers;

    final static Logger logger = Logger.getLogger(Ticket.class);

    public Ticket(Integer rows, Integer columns, Integer itemsPerRow, Integer totalNumberCount) {

        this.setColumns(columns);
        this.setRows(rows);
        this.setItemsPerRow(itemsPerRow);
        this.setTotalNumberCount(totalNumberCount);
        threadLocalRandom=ThreadLocalRandom.current();
        generateTicket();

    }


    private void generateTicket() {


        Integer rows=this.getRows();
        Integer columns=this.getColumns();
        Integer totalNumberCount=this.getTotalNumberCount();
        Integer itemsPerRow=this.getItemsPerRow();
        Integer rangePerRow=totalNumberCount/rows;
        List<Integer> lowerBoundList = getLowerBoundValuesToBeFilledPerRow(rangePerRow);
        List<Integer> upperBoundList = getUpperBoundValuesToBeFilledPerRow(rangePerRow);
        List<List<Integer>> ticket = new ArrayList<>(rows);

        for(Integer row=0;row< rows;row++) {

            ticket.add(row,new ArrayList<>(Collections.nCopies(columns, 0)));

            for(Integer column=0;column<itemsPerRow;column++) {

                Integer randomValue=generateRandomNumber(lowerBoundList.get(row),upperBoundList.get(row));
                Integer randomIndex=generateRandomNumber(0,columns);

                if((ticket.get(row).size() > 0 && ticket.get(row).contains(randomValue)) || (ticket.get(row).size() > 0 && ticket.get(row).get(randomIndex) > 0)){
                    column--;
                }
                else {
                    ticket.get(row).set(randomIndex,randomValue);
                }

            }

        }

        this.setTicketNumbers(ticket); ;
    }


    private ArrayList<Integer> getLowerBoundValuesToBeFilledPerRow(Integer rangePerRow) {


        ArrayList<Integer> lowerBoundList=new ArrayList<>();
        lowerBoundList.add(0,1);

        for(Integer row=1;row<rows;row++) {
            lowerBoundList.add((rangePerRow*row)+1);
        }

        return lowerBoundList;
    }

    private ArrayList<Integer> getUpperBoundValuesToBeFilledPerRow(int rangePerRow) {

        ArrayList<Integer> upperBoundList=new ArrayList<>();
        for(Integer row=0;row<rows;row++) {

            upperBoundList.add((rangePerRow*(row+1))+1);
        }

        return upperBoundList;
    }


    private Integer generateRandomNumber(Integer origin,Integer bound) {
        int randomValue=threadLocalRandom.nextInt(origin,bound);

        return randomValue;
    }


    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getItemsPerRow() {
        return itemsPerRow;
    }

    public void setItemsPerRow(Integer itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
    }

    public Integer getTotalNumberCount() {
        return totalNumberCount;
    }

    public void setTotalNumberCount(Integer totalNumberCount) {
        this.totalNumberCount = totalNumberCount;
    }

    public ThreadLocalRandom getThreadLocalRandom() {
        return threadLocalRandom;
    }

    public List<List<Integer>> getTicketNumbers() {
        return ticketNumbers;
    }

    public void setTicketNumbers(List<List<Integer>> ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }



    public void setThreadLocalRandom(ThreadLocalRandom threadLocalRandom) {
        this.threadLocalRandom = threadLocalRandom;
    }
}
