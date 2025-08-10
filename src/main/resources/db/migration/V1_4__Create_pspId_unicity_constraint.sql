alter table payment
    add constraint unique_pspId unique (psp_id);