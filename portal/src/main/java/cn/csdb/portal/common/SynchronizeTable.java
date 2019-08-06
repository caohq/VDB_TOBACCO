package cn.csdb.portal.common;

import cn.csdb.portal.model.SynchronizationTable;
import cn.csdb.portal.repository.SynchronizationTablesDao;
import cn.csdb.portal.service.FileImportService;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author jinbao
 * @Date 2019/5/15 14:17
 * @Description 定时同步 用户关联已有数据表创建的新数据表
 **/
public class SynchronizeTable {

    private FileImportService fileImportService;

    private SynchronizationTablesDao synchronizationTablesDao;

    public void setFileImportService(FileImportService fileImportService) {
        this.fileImportService = fileImportService;
    }

    public void setSynchronizationTablesDao(SynchronizationTablesDao synchronizationTablesDao) {
        this.synchronizationTablesDao = synchronizationTablesDao;
    }

    public void start() {
        Timer timer = new Timer();
        timer.schedule(new SynchronizeTableTimer(fileImportService, synchronizationTablesDao), 0, 1000L * 60 * 30);
    }
}

class SynchronizeTableTimer extends TimerTask {

    private FileImportService fileImportService;

    private SynchronizationTablesDao synchronizationTablesDao;

    SynchronizeTableTimer(FileImportService fileImportService, SynchronizationTablesDao synchronizationTablesDao) {
        this.fileImportService = fileImportService;
        this.synchronizationTablesDao = synchronizationTablesDao;
    }

    @Override
    public void run() {
        List<SynchronizationTable> all = synchronizationTablesDao.selectAllOfDataAssembler();

        for (SynchronizationTable synchronizationTable : all) {
            boolean needSynchronize = compareTime(synchronizationTable);
            if (needSynchronize) {
                SynchronizeTableTask synchronizeTableTask = new SynchronizeTableTask(fileImportService, synchronizationTablesDao, synchronizationTable);
                Thread t1 = new Thread(synchronizeTableTask);
                t1.start();
                System.out.println("同步了一个：" + synchronizationTable.getTableName());
            }
        }
        System.out.println("执行timerTask" + new Date().getTime());
    }

    /**
     * @return true:需要同步
     */
    private boolean compareTime(SynchronizationTable synchronizationTable) {
        Long frequency = synchronizationTable.getFrequency();
        if (frequency == 0) {
            return false;
        }
        Long lastModifyTime = synchronizationTable.getLastModifyTime();
        Long currentTimeMillis = System.currentTimeMillis();
        long l = currentTimeMillis - lastModifyTime;
        return l >= frequency;
    }

}

class SynchronizeTableTask implements Runnable {
    private FileImportService fileImportService;
    private SynchronizationTablesDao synchronizationTablesDao;
    private SynchronizationTable synchronizationTable;

    SynchronizeTableTask(FileImportService fileImportService, SynchronizationTablesDao synchronizationTablesDao, SynchronizationTable synchronizationTable) {
        this.fileImportService = fileImportService;
        this.synchronizationTablesDao = synchronizationTablesDao;
        this.synchronizationTable = synchronizationTable;
    }

    @Override
    public void run() {
        String tableName = synchronizationTable.getTableName();
        String sqlString = synchronizationTable.getSqlString();
        String subjectCode = synchronizationTable.getSubjectCode();
        String tempTableName = tableName.concat("_dataassembler");
        fileImportService.SynchronizeTask(sqlString, tempTableName, subjectCode, tableName);
        synchronizationTable.setLastModifyTime(new Date().getTime());
        synchronizationTablesDao.save(synchronizationTable);
    }
}
