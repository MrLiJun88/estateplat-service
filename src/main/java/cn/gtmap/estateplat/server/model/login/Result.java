/**
  * Copyright 2019 bejson.com 
  */
package cn.gtmap.estateplat.server.model.login;
import java.util.List;

/**
 * Auto-generated: 2019-04-17 17:33:23
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result {

    private List<Table> Table;
    private List<Status> Status;
    public void setTable(List<Table> Table) {
         this.Table = Table;
     }
     public List<Table> getTable() {
         return Table;
     }

    public void setStatus(List<Status> Status) {
         this.Status = Status;
     }
     public List<Status> getStatus() {
         return Status;
     }

}