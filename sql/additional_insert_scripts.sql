------------------------------------------------------------------------------------------------------------------------
-- �������� 3 ����� �� ����� �����������
------------------------------------------------------------------------------------------------------------------------
-- �������� ���� 1
insert into spots (name, lat, lon, accepted, adding_date, updating_date, "desc", space_type_id, user_id, moder_id)
values ('�������� �����1', 25.12, 199.3, false, '2001-07-07', null, '�������� �����1', 1, 1, null);
-- �������� ���� 2
insert into spots (name, lat, lon, accepted, adding_date, updating_date, "desc", space_type_id, user_id, moder_id)
values ('�������� �����2', 35.12, 139.3, false, '2022-07-07', null, '�������� �����2', 2, 1, null);
-- �������� ���� 3
insert into spots (name, lat, lon, accepted, adding_date, updating_date, "desc", space_type_id, user_id, moder_id)
values ('�������� �����3', 5.12, 99.321, false, '2009-07-07', null, '�������� �����3', 3, 1, null);

-- �������� ���� ������ 1 2 3 5 ��� ����� 1
insert into spots_sport_types (spot_id, sport_type_id)
values (1, 1), (1, 2), (1, 3), (1, 5);
-- �������� ���� ������ 6 7 ��� ����� 2
insert into spots_sport_types (spot_id, sport_type_id)
values (2, 6), (2, 7);
-- �������� ���� ������ 5 ��� ����� 3
insert into spots_sport_types (spot_id, sport_type_id)
values (3, 5);

-- �������� ���� ������ 1 2 ��� ����� 1
insert into spots_spot_types (spot_id, spot_type_id)
values (1, 1), (1, 2);
-- �������� ���� ������ 1 ��� ����� 2
insert into spots_spot_types (spot_id, spot_type_id)
values (2, 1);
-- �������� ���� ������ 2 ��� ����� 3
insert into spots_spot_types (spot_id, spot_type_id)
values (3, 2);


------------------------------------------------------------------------------------------------------------------------
--
------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------------


