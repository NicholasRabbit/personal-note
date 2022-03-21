SELECT
	a.*,
	GROUP_CONCAT( DISTINCT b.driver_name ) AS driverName,-- 司机去重
	c.taskFuel,
	c.taskMileage 
FROM
	(-- 查询车辆任务数量（时间区间内）
	SELECT
		a.car_number AS carNumber,
		sum( a.data_value ) AS taskNumber 
	FROM
		vehicles_analyse_car a 
	WHERE
		a.analyse_type = 'CAR_TASK_COUNT' 
		AND DATE_FORMAT( a.data_date, '%Y-%m' )= '2021-12' 
	GROUP BY
		a.car_number 
	) a -- 关联时间区间内司机的打卡
	LEFT JOIN vehicles_attendance b ON a.carNumber = b.car_num 
	AND ( b.end_time IS NULL OR DATE_FORMAT( b.end_time, '%Y-%m' )= '2021-12' ) 
	AND DATE_FORMAT( b.start_time, '%Y-%m' ) <= '2021-12'
	LEFT JOIN (-- 查询任务的里程和油耗（时间区间内）
	SELECT
		ta.carNum,
		cast(
		sum( ta.fuel ) AS DECIMAL ( 15, 2 )) AS taskFuel,
		cast(
		sum( ta.load_mileage ) AS DECIMAL ( 15, 2 )) AS taskMileage 
	FROM
		long_muck_task ta 
	WHERE
		DATE_FORMAT( ta.start_time, '%Y-%m' )= '2021-12' 
		OR DATE_FORMAT( ta.end_time, '%Y-%m' )= '2021-12' 
	GROUP BY
		ta.carNum 
	) c ON a.carNumber = c.carNum 
GROUP BY
	a.carNumber,
	a.taskNumber,
	c.taskFuel,
	c.taskMileage 
ORDER BY
	a.taskNumber DESC