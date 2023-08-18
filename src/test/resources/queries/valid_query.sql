WITH subquery_1 AS ( SELECT * FROM `lofty-root-378503.test_dataset_integration.test_table_integration` WHERE id LIKE "%asdf-1234%" AND column_b LIKE "%column_b_val_1%" AND column_a LIKE "%column_a_val_1%" ), subquery_2 AS ( SELECT * FROM subquery_1 ) SELECT id, creation_timestamp, last_update_timestamp, column_a, column_b FROM subquery_2 GROUP BY id, creation_timestamp, last_update_timestamp, column_a, column_b;