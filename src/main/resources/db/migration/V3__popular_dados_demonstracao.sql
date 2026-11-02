INSERT INTO T_CLYVO_RESPONSAVEL (nm_responsavel, cpf, dt_nascimento, email, num_celular)
VALUES ('Mariana Souza', '111.444.777-35', DATE '1990-03-12', 'mariana.souza@email.com', '(11) 98888-1111');

INSERT INTO T_CLYVO_RESPONSAVEL (nm_responsavel, cpf, dt_nascimento, email, num_celular)
VALUES ('Carlos Almeida', '123.456.789-09', DATE '1985-07-25', 'carlos.almeida@email.com', '(11) 97777-2222');

INSERT INTO T_CLYVO_VETERINARIO (nm_veterinario, cpf, dt_nascimento, email, num_celular, especialidade)
VALUES ('Rafael Lima', NULL, DATE '1988-02-10', 'rafael.lima@clyvo.com.br', '(11) 96666-3333', 'Clinica Geral');

INSERT INTO T_CLYVO_VETERINARIO (nm_veterinario, cpf, dt_nascimento, email, num_celular, especialidade)
VALUES ('Camila Torres', NULL, DATE '1992-11-03', 'camila.torres@clyvo.com.br', '(11) 95555-4444', 'Dermatologia');

INSERT INTO T_CLYVO_ANIMAL (nm_animal, idade, especie, raca, sexo, dt_nascimento, peso, fk_id_responsavel)
VALUES ('Thor', 0, 'Cachorro', 'Golden Retriever', 'M', ADD_MONTHS(TRUNC(SYSDATE), -8), 18.5,
    (SELECT MAX(id_responsavel) FROM T_CLYVO_RESPONSAVEL WHERE email = 'mariana.souza@email.com'));

INSERT INTO T_CLYVO_ANIMAL (nm_animal, idade, especie, raca, sexo, dt_nascimento, peso, fk_id_responsavel)
VALUES ('Mel', 3, 'Gato', 'SRD', 'F', ADD_MONTHS(TRUNC(SYSDATE), -36), 4.2,
    (SELECT MAX(id_responsavel) FROM T_CLYVO_RESPONSAVEL WHERE email = 'mariana.souza@email.com'));

INSERT INTO T_CLYVO_ANIMAL (nm_animal, idade, especie, raca, sexo, dt_nascimento, peso, fk_id_responsavel)
VALUES ('Bob', 10, 'Cachorro', 'Poodle', 'M', ADD_MONTHS(TRUNC(SYSDATE), -120), 7.8,
    (SELECT MAX(id_responsavel) FROM T_CLYVO_RESPONSAVEL WHERE email = 'carlos.almeida@email.com'));

INSERT INTO T_CLYVO_ANIMAL (nm_animal, idade, especie, raca, sexo, dt_nascimento, peso, fk_id_responsavel)
VALUES ('Pipoca', 1, 'Coelho', 'Mini Lop', 'F', ADD_MONTHS(TRUNC(SYSDATE), -14), 1.6,
    (SELECT MAX(id_responsavel) FROM T_CLYVO_RESPONSAVEL WHERE email = 'carlos.almeida@email.com'));

INSERT INTO T_CLYVO_VACINA (nm_vacina, tipo, descricao)
VALUES ('V10', 'Polivalente', 'Proteção contra as principais doencas dos cães.');

INSERT INTO T_CLYVO_VACINA (nm_vacina, tipo, descricao)
VALUES ('V4', 'Polivalente', 'Proteção contra as principais doencas dos gatos.');

INSERT INTO T_CLYVO_VACINA (nm_vacina, tipo, descricao)
VALUES ('Antirrabica', 'Obrigatória', 'Proteção contra a raiva.');

INSERT INTO T_CLYVO_DOENCA (nm_doenca, tipo, contagiosidade, descricao)
VALUES ('Otite', 'Inflamatória', 'N', 'Inflamação no ouvido.');

INSERT INTO T_CLYVO_DOENCA (nm_doenca, tipo, contagiosidade, descricao)
VALUES ('Dermatite', 'Alérgica', 'N', 'Inflamação na pele.');

INSERT INTO T_CLYVO_VACINA_ANIMAL (num_doses, frequencia_aplicacao, dt_receita, fk_id_vacina, fk_id_animal)
VALUES (3, 21, TRUNC(SYSDATE) - 45,
    (SELECT MAX(id_vacina) FROM T_CLYVO_VACINA WHERE nm_vacina = 'V10'),
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Thor'));

INSERT INTO T_CLYVO_VACINA_ANIMAL (num_doses, frequencia_aplicacao, dt_receita, fk_id_vacina, fk_id_animal)
VALUES (3, 21, TRUNC(SYSDATE) - 24,
    (SELECT MAX(id_vacina) FROM T_CLYVO_VACINA WHERE nm_vacina = 'V10'),
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Thor'));

INSERT INTO T_CLYVO_VACINA_ANIMAL (num_doses, frequencia_aplicacao, dt_receita, fk_id_vacina, fk_id_animal)
VALUES (3, 21, TRUNC(SYSDATE) - 10,
    (SELECT MAX(id_vacina) FROM T_CLYVO_VACINA WHERE nm_vacina = 'V4'),
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Mel'));

INSERT INTO T_CLYVO_VACINA_ANIMAL (num_doses, frequencia_aplicacao, dt_receita, fk_id_vacina, fk_id_animal)
VALUES (2, 30, TRUNC(SYSDATE) - 28,
    (SELECT MAX(id_vacina) FROM T_CLYVO_VACINA WHERE nm_vacina = 'V10'),
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Bob'));

INSERT INTO T_CLYVO_VACINA_ANIMAL (num_doses, frequencia_aplicacao, dt_receita, fk_id_vacina, fk_id_animal)
VALUES (1, 365, TRUNC(SYSDATE) - 100,
    (SELECT MAX(id_vacina) FROM T_CLYVO_VACINA WHERE nm_vacina = 'Antirrabica'),
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Bob'));

INSERT INTO T_CLYVO_CONSULTA (dt_agendamento, dt_consulta, fk_id_animal, fk_id_veterinario)
VALUES (TRUNC(SYSDATE) - 35, TRUNC(SYSDATE) - 30,
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Bob'),
    (SELECT MAX(id_veterinario) FROM T_CLYVO_VETERINARIO WHERE email = 'rafael.lima@clyvo.com.br'));

INSERT INTO T_CLYVO_CONSULTA (dt_agendamento, dt_consulta, fk_id_animal, fk_id_veterinario)
VALUES (TRUNC(SYSDATE), TRUNC(SYSDATE) + 2,
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Thor'),
    (SELECT MAX(id_veterinario) FROM T_CLYVO_VETERINARIO WHERE email = 'rafael.lima@clyvo.com.br'));

INSERT INTO T_CLYVO_CONSULTA (dt_agendamento, dt_consulta, fk_id_animal, fk_id_veterinario)
VALUES (TRUNC(SYSDATE), TRUNC(SYSDATE) + 5,
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Mel'),
    (SELECT MAX(id_veterinario) FROM T_CLYVO_VETERINARIO WHERE email = 'camila.torres@clyvo.com.br'));

INSERT INTO T_CLYVO_CONSULTA (dt_agendamento, dt_consulta, fk_id_animal, fk_id_veterinario)
VALUES (TRUNC(SYSDATE), TRUNC(SYSDATE) + 20,
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Bob'),
    (SELECT MAX(id_veterinario) FROM T_CLYVO_VETERINARIO WHERE email = 'rafael.lima@clyvo.com.br'));

INSERT INTO T_CLYVO_DOENCA_ANIMAL (dt_diagnostico, gravidade, status, observacoes, fk_id_animal, fk_id_doenca)
VALUES (TRUNC(SYSDATE) - 60, 'Leve', 'Em tratamento', 'Acompanhamento em consulta de retorno.',
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Bob'),
    (SELECT MAX(id_doenca) FROM T_CLYVO_DOENCA WHERE nm_doenca = 'Otite'));

INSERT INTO T_CLYVO_DOENCA_ANIMAL (dt_diagnostico, gravidade, status, observacoes, fk_id_animal, fk_id_doenca)
VALUES (TRUNC(SYSDATE) - 200, 'Moderada', 'Controlada', 'Alergia sazonal.',
    (SELECT MAX(id_animal) FROM T_CLYVO_ANIMAL WHERE nm_animal = 'Mel'),
    (SELECT MAX(id_doenca) FROM T_CLYVO_DOENCA WHERE nm_doenca = 'Dermatite'));