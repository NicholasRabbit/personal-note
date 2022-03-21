 @Transactional
    @Scheduled(cron="0 0/15 4-22 * * ?")
    public void getCarInfo(){

        logger.info("==========添加静止时间任务开始==========:"+DateUtils.getDateTime());

        String formName = "f3_location_" + DateFormatUtils.format( System.currentTimeMillis(), "yyyyMMdd");  //查询当天数据

        List<BasicsCdoRelation>  allCarList = bcrDao.findAllList(new BasicsCdoRelation());     //获取所有在用车辆的信息，根据其id来查询f3_location_..
        Map<String, StopTimeInfo> stopMap = bilDao.getLastTime(allCarList);                    //从静止表查询每辆车的最后一条时间段
        endTime = JedisUtils.get("key_stop_endTime");
        Map<String,BasicsCdoRelation> bcrMap = bilDao.getAllSpeed(formName,endTime);          //从f3表内获取一个时间段内所有车辆信息，根据band_id放到Map集合中
        List<BasicsIndexLocation>  endTimeList = new ArrayList<>();     //所有时间的集合，用来获取该时间段的最后时间
        Set<String>  keySet = bcrMap.keySet();
        for(String  bilKey : keySet){
            endTimeList.addAll(bcrMap.get(bilKey).getBilList());   //把Map所有的BasicsIndexLocation放到一个集合里，
        }
        Collections.sort(endTimeList,new BasiccIndexLocationComparator());    //对集合排序，求出最后一条的时间
        if(endTimeList.size() > 0){
            for(int i = endTimeList.size()-1; i > 0; i--){
                String lastTime = endTimeList.get(i).getTime();
                if(DateUtils.getStringMillisecond(lastTime,DateUtils.getDateTime()) < 1000*60*60*24){
                    endTime = lastTime;
                    break;
                }
            }

        }
        JedisUtils.set("key_stop_endTime",endTime,60*60*24*5);
        List<StopTimeInfo>  stopList = new ArrayList<>();           //预先建好集合用来保存含有起止时间的对象
        for(String bandId : keySet){
            List<BasicsIndexLocation> bilList = bcrMap.get(bandId).getBilList();     //根据获取每个车band_id的速度时间信息的集合
                StopTimeInfo sti = new StopTimeInfo();
                StopTimeInfo stiUpdate = new StopTimeInfo();
                stiUpdate = stopMap.get(bandId);     //获取endTime为null的对象
                boolean alwaysZero = false;
                for(BasicsIndexLocation b : bilList){
                    if(!stopMap.containsKey(bandId) || (stopMap.containsKey(bandId) && stiUpdate.getEndTime() != null)) {            //如果静止表内没有该车信息，或者最后一条的结束时间有，则添加新的一条
                        if (b.getSpeed() <= 5 && sti.getBeginTime() == null) {
                            sti.setBeginTime(b.getTime());
                            sti.setBandId(bandId);
                            sti.setCarNum(bcrMap.get(bandId).getCarNum());
                            sti.setLatitude(b.getLatitude());
                            sti.setLongitude(b.getLongitude());
                        } else if (b.getSpeed() > 5 && sti.getBeginTime() != null) {
                            sti.setEndTime(b.getTime());
                            long millisGap = DateUtils.getStringMillisecond(sti.getEndTime(), sti.getBeginTime());
                            sti.setStopTime(millisGap);
                            if (millisGap > 1000 * 60 * 2) {            //小于120秒的停止时间不添加，防止误判,高速收费口
                                sti.setBandId(bandId);
                                stopList.add(sti);
                            }
                            sti = new StopTimeInfo();
                        }
                    }
                    if(stopMap.containsKey(bandId) && stiUpdate.getEndTime() == null){
                        if(b.getSpeed() > 5){
                            stiUpdate.setEndTime(b.getTime());
                            long millisGap = DateUtils.getStringMillisecond(stiUpdate.getEndTime(), stiUpdate.getBeginTime());
                            stiUpdate.setStopTime(millisGap);
                            if (millisGap > 1000 * 60 * 2) {            //小于120秒的停止时间不添加，防止误判,高速收费口
                                bilDao.updateEndTime(stiUpdate);
                                alwaysZero = false;
                            }
                        }else{    //新的一个时间段速度全为0的情况，需更行stopTime
                            long stopTimeZero = DateUtils.getStringMillisecond(DateUtils.getDateTime(), stiUpdate.getBeginTime());
                            stiUpdate.setStopTime(stopTimeZero);
                            alwaysZero = true;
                        }
                    }
                }
                if(alwaysZero){
                    bilDao.updateEndTime(stiUpdate);     //如果新的时间段速度全是0，则更新静止时间
                }
                if(sti.getBandId() != null &&  sti.getBeginTime() != null){
                    long stopTime = DateUtils.getStringMillisecond(DateUtils.getDateTime(), sti.getBeginTime());   //如果速度一直为0则当前时间减去开始时间，更新到表中
                    sti.setStopTime(stopTime);
                    stopList.add(sti);     //如果速度在这个时间段为null,此时添加的结束时间是空
                }
        }

        if(stopList.size() > 0){               //把一辆车的静止时间静止时间批量进静止表，空集合时不添加，防止sql语法错误
            bilDao.insertAll(stopList);
            bilDao.backupStopTime(stopList);
        }
        logger.info("==========添加静止时间任务结束==========:"+DateUtils.getDateTime());

    }