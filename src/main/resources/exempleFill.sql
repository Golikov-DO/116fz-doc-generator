ROLLBACK;

--команды для переноса бд
--создание дампа
--docker exec -t DocumentsDB pg_dump -U Admin -f /backup.sql PMLLPA
--перенос на гугл диск
--docker cp DocumentsDB:/backup.sql "J:\Google диск\Програмирование\Java\db\backup.sql"
--очищаем временный контейнер
-- docker exec -t DocumentsDB rm /backup.sql

--запись данных в контейнер из дампа
--docker cp "F:\Google диск\Програмирование\Java\db\backup.sql" DocumentsDB:/backup.sql
--записываем в уже созданный контейнер
--docker exec -it newDocumentsDB psql -U Admin -d PMLLPA -f /backup.sql
--очищаем временный контейнер
--docker exec -it newDocumentsDB rm /backup.sql

DO $$
    DECLARE
        t_name TEXT;
        table_list TEXT[] := ARRAY[
            'asf', 'asf_certificate', 'asf_composition_deployment_funds',
            'asf_document_image', 'asf_personnel', 'asf_signer',
            'asf_specialists', 'asf_work_type', 'emergency_services',
            'hazardous_param', 'hazardous_param_value', 'hazardous_substance',
            'object', 'object_address', 'object_city', 'object_composition_kchs',
            'object_development_accident_scenarios', 'object_image',
            'object_insurance_policy', 'object_main_scenarios',
            'object_order_minimum_balance', 'object_primary_fire_extinguishing_equipment',
            'object_regional_authorities', 'object_responsible_persons',
            'object_structure', 'object_technological_block',
            'object_technological_equipment', 'object_type', 'organization',
            'organization_address', 'organization_contact', 'organization_signer',
            'table_title'
            ];
    BEGIN
        FOREACH t_name IN ARRAY table_list LOOP
                EXECUTE format(
                        'SELECT setval(pg_get_serial_sequence(''public.%I'', ''id''), COALESCE(MAX(id), 0) + 1, false) FROM public.%I',
                        t_name, t_name
                        );
            END LOOP;
    END $$;

