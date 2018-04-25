.print 1
select first_name, last_name from mentors;

.print 2
select nick_name from mentors where city like  "'Misko%";

.print 3
select (first_name || last_name) as full_name, phone_num
from applicants
where first_name is "'Carol'" ;

.print 4
select (first_name || last_name) as full_name, phone_num
from applicants
where email like "%@adipi%" ;

.print 5
insert into applicants(first_name, last_name, phone_num, email, application_code) 
values("'Markus'", "'Schaffarzyk'", "'003620/725-2666'", "'djnovus@groovecoverage.com'", 54823);
select * from applicants
where application_code is 54823;

.print 6
update applicants
set phone_num = "'003670/223-7459'"
where first_name is "'Jemima'" and last_name is "'Foreman'";
select  phone_num from applicants
where first_name is "'Jemima'" and last_name is "'Foreman'";

.print 7
delete from applicants
where email like '%@mauriseu%';


