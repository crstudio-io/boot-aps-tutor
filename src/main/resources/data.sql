insert into USER_TABLE(email) values ('aquashdw@gmail.com');
insert into ROLE_TABLE(name) values ('ADMIN'), ('MAINTAINER'), ('USER');
insert into USER_ROLES(user_id, role_id) values (1, 1);

insert into PROBLEM(user_id, title, prob_desc, input_desc, output_desc, timeout, memory)
values (1, '입출력', '간단한 입출력을 연습하자.', '한줄의 데이터가 입력으로 주어진다.', '해당 데이터를 아무 변경 없이 출력한다.', 10, 512);

insert into TEST_CASE(user_id, input, output, prob_id)
values (1, E'Hello, World!\n', E'Hello, World!\n', 1),
       (1, E'Hi\n', E'Hi\n', 1),
       (1, E'Goodbye\n', E'Goodbye\n', 1);

insert into IO_EXAMPLE(input_example, output_example, description, prob_id)
values (E'Hello, World!\n', E'Hello, World!\n', null, 1);

insert into SOLUTION(prob_id, user_id, lang, status, score, code)
values (1, 1, 'JAVA17', 'SUCCESS', 100, E'import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        System.out.println(scanner.nextLine());\n    }\n}');
