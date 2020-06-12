package com.tao.code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class ZoneTable {
    private List<Zone> table = new ArrayList<>();
    private Vector<String> titleName = new Vector<>();
    private int size;
    public int getSize() {
    	return size;
    }
    public List<Zone> getTable(){
    	return table;
    }
    public ZoneTable(int size) {
        titleName.add("区号");
        titleName.add("起始");
        titleName.add("大小");
        titleName.add("状态");
        titleName.add("作业名");
        this.size=size;
        // 初始化的时候清空table
        table.clear();
        table.add(Zone.init(size));
    }

    public boolean add(Job job) {
        boolean success = false;
        if (job.getSize() <= 0) {
            return false;
        }
        for (Zone z : table) {
            if (z.getState().equals(State.FREE) && z.getSize() >= job.getSize()) {
                success = true;
                table.add(z.allot(job));
                if (z.getSize() == 0) {
                    table.remove(z);
                }
                break;
            }
        }
        return success;
    }

    public boolean free(int no) {
        if (no < 1 || no > table.size()) {
            return false;
        }
        
        Zone target=null;
        for(Zone z:table) {
        	if(z.getNo()==no) {
        		target = z;
        		break;
        	}
        }
        if (!target.getState().equals(State.FREE)) {
            target.free();
            Zone pre = null;
            Zone next = null;

            if (no != 1) {
                for(Zone z:table) {
                	if(z.getNo()==no-1) {
                		pre = z;
                		break;
                	}
                }
            }
            if (no != table.size()) {
                for(Zone z:table) {
                	if(z.getNo()==no+1) {
                		next = z;
                		break;
                	}
                }
            }
            if (pre != null && pre.getState().equals(State.FREE)) {
                pre.merge(target);
                table.remove(target);
                if (next != null && next.getState().equals(State.FREE)) {
                    pre.merge(next);
                    table.remove(next);
                }
            } else if (next != null && next.getState().equals(State.FREE)) {
                next.merge(target);
                table.remove(target);
            }
        } else {
            return false;
        }
        return true;
    }

    private void resetNo() {
        for (int i = 0; i < table.size(); i++) {
            table.get(i).setNo(i + 1);
        }
    }

    public void updateData(Vector data,String str) {
    	table = table.stream().sorted(Comparator.comparing(Zone::getBegin)).collect(Collectors.toList());
    	resetNo();
        data.clear();
        for (Zone zone : table) {
            Vector temp = new Vector();
            temp.add(zone.getNo());
            System.out.println(zone.getNo());
            temp.add(zone.getBegin());
            temp.add(zone.getSize());
            temp.add(zone.getState().toString());
            if (zone.getState().equals(State.BUSY)) {
                temp.add(zone.getJob().getName());
            } else {
                temp.add("无作业");
            }
            System.out.println("no=" + zone.getNo() + "begin=" + zone.getBegin() + "size=" + zone.getSize() + "sate=" + zone.getState().toString());
            data.add(temp);
        }
        if(str.compareTo("首次适应算法")==0)
    		;
    	else if(str.compareTo("最佳适应算法")==0)
    		table = table.stream().sorted(Comparator.comparing(Zone::getSize)).collect(Collectors.toList());
    	else if(str.compareTo("最坏适应算法")==0)
    		table = table.stream().sorted(Comparator.comparing(Zone::getSize).reversed()).collect(Collectors.toList());
        

    }

    public Vector<String> getTitleName() {
        return titleName;
    }
}
