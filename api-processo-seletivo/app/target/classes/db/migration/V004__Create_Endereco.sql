CREATE TABLE endereco (
    end_id SERIAL PRIMARY KEY,
	end_tipo_logradouro VARCHAR(50) NOT NULL,
    end_logradouro VARCHAR(200) NOT NULL,
    end_numero INT,
    end_bairro VARCHAR(100) NOT NULL,
    cid_id INT NOT NULL,
    FOREIGN KEY (cid_id) REFERENCES cidade(cid_id)
);
