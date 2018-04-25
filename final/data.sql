.mode csv
.import mentors.csv mentorsT
.import applicants.csv applicantsT
insert into mentors(first_name, last_name, nick_name, phone_num, email, city, fav_num)
select * from mentorsT;
insert into applicants(first_name, last_name, phone_num, email, application_code)
select * from applicantsT;
drop table mentorsT;
drop table applicantsT;
select * from mentors;
