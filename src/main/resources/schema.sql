create table if not exists quizzes(
    id UUID primary key,
    topic text not null
);

create table if not exists questions(
    id UUID primary key,
    question_value text not null,
    sent_at timestamp,
    quiz_id UUID not null,
    constraint quiz_fk
        foreign key (quiz_id)
        references quizzes (id)
);

create table if not exists answers(
    id UUID primary key,
    answer_value text not null,
    is_correct_answer boolean not null default false,
    question_id UUID not null,
    constraint question_fk
        foreign key (question_id)
        references questions (id)
);

create table if not exists users(
    id UUID primary key,
    username varchar(30) not null
);

create table if not exists rooms(
    id UUID primary key,
    room_code char(8) not null unique,
    is_public boolean not null default true,
    wait_time integer not null default 15,
    max_number_players integer not null,
    quiz_id UUID,
    owner_id UUID not null unique,
    constraint room_quiz_fk
        foreign key (quiz_id)
        references quizzes (id),
    constraint room_user_fk
            foreign key (owner_id)
            references users (id)
);

create table if not exists scores(
    id UUID primary key,
    score integer not null default 0,
    user_id UUID not null unique,
    room_id UUID not null
);