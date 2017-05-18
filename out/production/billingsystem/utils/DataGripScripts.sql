

select * from get_student_registration('2004-2005','1','2004-0001');
select * from student;
select unitsload('2004-0001','2004-2005','1');

select * from semsubject;
select * from registration WHERE studid = '2004-0001';

select * from semester where current=true;

select * from semester where current=true;
select studlevel from semstudent;

-- Students Basic Info
SELECT fname.fullname2 as name, semstud.studlevel, semstud.studmajor as course  FROM studfullnames fname, student stud, semstudent semstud
WHERE fname.studid=stud."studid" AND semstud.studid=stud.studid AND
semstud.sy = '2004-2005' AND semstud.sem = '1';
--

-- Students Certificate of Registration
select subject.subjcode,subjlec_units,subjlab_units, CAST(subjcredit as REAL) as subjcred_units,
  unitsload('2004-0001','2004-2005','1'), registration.section from subject, registration
where registration.subjcode=subject.subjcode AND studid='2004-0001' and sy='2004-2005' and registration.sem='1';
--
select * from registration limit 0;
select * from or_brkdown;


select * from semstudent;

select * from registration;

-- or no

-- total tuiton amount payed
-- total misc amount payed
-- tuiton balance
-- misc balance

-- Total MISC and TUITION fees
select sy,sem,SUM(amt) as totalmisc, totaltuition from ass_details LEFT JOIN
  (select amt as totaltuition,sy,sem from ass_details where
    feecode='TUITIONFEE' and studid='2004-0001' GROUP BY sy,sem,amt) as tuitiondetails
  USING(sy,sem)
where feecode != 'TUITIONFEE' and studid='2004-0001' GROUP BY sy,sem,totaltuition ORDER BY sy, sem;
--

-- Total TUITION fee
select sy,sem,amt as totaltuition from ass_details where feecode = 'TUITIONFEE' and studid='2004-0001' GROUP BY sy,sem,amt ORDER BY sy,sem;
-- Total MISC fee
select sy,sem,amt as totalmisc from ass_details where feecode != 'TUITIONFEE' and studid='2004-0001' GROUP BY sy,sem,amt ORDER BY sy,sem;
--


------------- Total Amount Payed and Balance All SY and Sem
SELECT sy,sem, totalpay,totalbalance FROM
  (SELECT sy,sem,(total_pay+total_orbrkdwn) as totalpay FROM
  (SELECT sy,sem,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay
                  FROM payment
                  WHERE paycode='CE' AND studid='2005-2006' GROUP BY sy,sem,orno) as pp
  LEFT JOIN
  (SELECT sy,sem,orno,sum(coalesce(amount,0.00)) as total_orbrkdwn
                  FROM or_brkdown LEFT JOIN payment USING(orno)
                  WHERE or_brkdown.studid = '2005-2006' GROUP BY sy,sem,orno) as oo USING(sy,sem,orno)) as ppp
LEFT JOIN (SELECT sy,sem,(total_assess-(total_pay+total_orbrkdwn)) as totalbalance from
  (SELECT sy,sem,studid,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay
                  FROM payment
                  WHERE paycode='CE' AND studid='2005-2006' GROUP BY orno) as pp
  LEFT JOIN
  (SELECT sy,sem,or_brkdown.studid,orno,sum(coalesce(amount,0.00)) as total_orbrkdwn
                  FROM or_brkdown LEFT JOIN payment USING(orno)
                  WHERE or_brkdown.studid = '2005-2006' GROUP BY sy,sem,or_brkdown.studid,orno) as oo USING(sy,sem,studid)
  LEFT JOIN
  (SELECT sy,sem,studid,(coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00)) as total_assess
                  FROM assessment) as aa USING (sy,sem,studid)) as bbb USING(sy,sem);
--------------------------------------


-- Total Amount Payed All SY and Sem
SELECT sy,sem,(total_pay+total_orbrkdwn) as totalpay FROM
  (SELECT sy,sem,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay
                  FROM payment
                  WHERE paycode='CE' AND studid='2005-2006' GROUP BY sy,sem,orno) as pp
  LEFT JOIN
  (SELECT sy,sem,orno,sum(coalesce(amount,0.00)) as total_orbrkdwn
                  FROM or_brkdown LEFT JOIN payment USING(orno)
                  WHERE or_brkdown.studid = '2005-2006' GROUP BY sy,sem,orno) as oo USING(sy,sem,orno);
--

--Total amount payed per sem
SELECT (total_pay+total_orbrkdwn) as totalpay from
  (SELECT orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay
                  FROM payment
                  WHERE sy='2005-2006' AND sem='1' AND paycode='CE' AND studid='2005-2006' GROUP BY orno) as pp
  LEFT JOIN
  (SELECT orno,sum(coalesce(amount,0.00)) as total_orbrkdwn
                  FROM or_brkdown LEFT JOIN payment USING(orno)
                  WHERE sy='2005-2006' AND sem='1' AND or_brkdown.studid = '2005-2006' GROUP BY orno) as oo USING(orno);
--

--Total balance per sem
SELECT (total_assess-(total_,sum(coalesce(amount,0.00)) as total_orbrkdwn
                  FROM or_brkdopay+total_orbrkdwn)) as totalbalance from
  (SELECT studid,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay
                  FROM payment
                  WHERE sy='2005-2006' AND sem='1' AND paycode='CE' AND studid='2005-2006' GROUP BY orno) as pp
  LEFT JOIN
  (SELECT or_brkdown.studid,ornown LEFT JOIN payment USING(orno)
                  WHERE sy='2005-2006' AND sem='1' AND or_brkdown.studid = '2005-2006' GROUP BY or_brkdown.studid,orno) as oo USING(studid)
  LEFT JOIN
  (SELECT studid,(coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00)) as total_assess
                  FROM assessment
                  WHERE sy='2005-2006' AND sem='1') as aa USING (studid);
--

-- Total Balance All SY and Sem
SELECT sy,sem,(total_assess-(total_pay+total_orbrkdwn)) as totalbalance from
  (SELECT sy,sem,studid,orno,sum((coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00))) as total_pay
                  FROM payment
                  WHERE paycode='CE' AND studid='2005-2006' GROUP BY orno) as pp
  LEFT JOIN
  (SELECT sy,sem,or_brkdown.studid,orno,sum(coalesce(amount,0.00)) as total_orbrkdwn
                  FROM or_brkdown LEFT JOIN payment USING(orno)
                  WHERE or_brkdown.studid = '2005-2006' GROUP BY sy,sem,or_brkdown.studid,orno) as oo USING(sy,sem,studid)
  LEFT JOIN
  (SELECT sy,sem,studid,(coalesce(regfee,0.00)+coalesce(labfee,0.00)+coalesce(compfee,0.00)+coalesce(libfee,0.00)+coalesce(athlfee,0.00)+coalesce(medfee,0.00)+coalesce(spubfee,0.00)+coalesce(sgovfee,0.00)+coalesce(studentfee,0.00)+coalesce(tuitionfee,0.00)+coalesce(latefee,0.00)+coalesce(idfee,0.00)+coalesce(guidancefee,0.00)+coalesce(facilitiesfee,0.00)) as total_assess
                  FROM assessment) as aa USING (sy,sem,studid);

--


creates