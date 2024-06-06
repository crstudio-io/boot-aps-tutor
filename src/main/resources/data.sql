insert into USER_TABLE(email) values ('aquashdw@gmail.com');

insert into PROBLEM(prob_desc, input_desc, output_desc, timeout, memory)
values ('간단한 입출력을 연습하자.', '한줄의 데이터가 입력으로 주어진다.', '해당 데이터를 아무 변경 없이 출력한다.', 10, 512);

insert into TEST_CASE(input, output, prob_id)
values (E'Hello, World!\n', E'Hello, World!\n', 1);
insert into TEST_CASE(input, output, prob_id)
values (E'Hi\n', E'Hi\n', 1);
insert into TEST_CASE(input, output, prob_id)
values (E'Goodbye\n', E'Goodbye\n', 1);

insert into IO_EXAMPLE(input_example, output_example, description, prob_id)
values (E'Hello, World!\n', E'Hello, World!\n', null, 1);