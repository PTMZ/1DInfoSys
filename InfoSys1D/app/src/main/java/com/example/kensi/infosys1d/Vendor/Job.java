package com.example.kensi.infosys1d.Vendor;

public class Job {
    private int taskId;
    private String itemName;
    private int qty;
    private int tableId;

    public Job(int taskId, String itemName, int qty, int tableId){
        this.taskId = taskId;
        this.itemName = itemName;
        this.qty = qty;
        this.tableId = tableId;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQty() {
        return qty;
    }

    public int getTableId() {
        return tableId;
    }
}
