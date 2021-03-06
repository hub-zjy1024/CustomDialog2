package com.zjy.js.customdialog.printer;

import android.print.PrintAttributes;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrinterDiscoverySession;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 Created by 张建宇 on 2017/4/13. */

public class MyPrintDiscoverySession extends PrinterDiscoverySession {
    private static final String TAG = "MyPrintDiscoverySession";
    private final MyPrintService myPrintService;

    public MyPrintDiscoverySession (MyPrintService myPrintService) {
        Log.e(TAG, "MyPrintDiscoverySession()");
        this.myPrintService = myPrintService;
    }

    @Override
    public void onStartPrinterDiscovery(List<PrinterId> priorityList) {
        Log.e(TAG, "onStartPrinterDiscovery()");
        List<PrinterInfo> printers = this.getPrinters();
        Log.e("zjy", "MyPrintDiscoverySession->onStartPrinterDiscovery(): printers==" + printers.size());
        String name = "printer1";
        PrinterInfo myprinter = new PrinterInfo
                .Builder(myPrintService.generatePrinterId(name), name, PrinterInfo.STATUS_IDLE)
                .build();
        printers.add(myprinter);
        addPrinters(printers);
    }

    @Override
    public void onStopPrinterDiscovery() {
        Log.e(TAG, "onStopPrinterDiscovery()");
    }

    /**
     * 确定这些打印机存在
     * @param printerIds
     */
    @Override
    public void onValidatePrinters(List<PrinterId> printerIds) {
        Log.e(TAG, "onValidatePrinters()");
    }

    /**
     * 选择打印机时调用该方法更新打印机的状态，能力
     * @param printerId
     */
    @Override
    public void onStartPrinterStateTracking(PrinterId printerId) {
        Log.e(TAG, "onStartPrinterStateTracking()");
        PrinterInfo printer = findPrinterInfo(printerId);
        if (printer != null) {
            PrinterCapabilitiesInfo capabilities =
                    new PrinterCapabilitiesInfo.Builder(printerId)
                            .setMinMargins(new PrintAttributes.Margins(200, 200, 200, 200))
                            .addMediaSize(PrintAttributes.MediaSize.ISO_A4, true)
                            //.addMediaSize(PrintAttributes.MediaSize.ISO_A5, false)
                            .addResolution(new PrintAttributes.Resolution("R1", "200x200", 200, 200), false)
                            .addResolution(new PrintAttributes.Resolution("R2", "300x300", 300, 300), true)
                            .setColorModes(PrintAttributes.COLOR_MODE_COLOR
                                            | PrintAttributes.COLOR_MODE_MONOCHROME,
                                    PrintAttributes.COLOR_MODE_MONOCHROME)
                            .build();

            printer = new PrinterInfo.Builder(printer)
                    .setCapabilities(capabilities)
                    .setStatus(PrinterInfo.STATUS_IDLE)
                    //        .setDescription("fake print 1!")
                    .build();
            List<PrinterInfo> printers = new ArrayList<PrinterInfo>();

            printers.add(printer);
            addPrinters(printers);
        }
    }

    @Override
    public void onStopPrinterStateTracking(PrinterId printerId) {
        Log.e(TAG, "onStopPrinterStateTracking()");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
    }

    private PrinterInfo findPrinterInfo(PrinterId printerId) {
        List<PrinterInfo> printers = getPrinters();
        final int printerCount = getPrinters().size();
        for (int i = 0; i < printerCount; i++) {
            PrinterInfo printer = printers.get(i);
            if (printer.getId().equals(printerId)) {
                return printer;
            }
        }
        return null;
    }

}
