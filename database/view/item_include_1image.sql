CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `mp2`@`%` 
    SQL SECURITY DEFINER
VIEW `item_include_1image` AS
    SELECT 
        `item`.`item_no` AS `item_no`,
        `item`.`item_price` AS `item_price`,
        `item`.`item_quality` AS `item_quality`,
        `item`.`item_title` AS `item_title`,
        `item`.`item_view_cnt` AS `item_view_cnt`,
        `item`.`item_sale_start_date` AS `item_sale_start_date`,
        `item`.`item_sale_end_date` AS `item_sale_end_date`,
        `user`.`user_nick` AS `user_nick`,
        `image`.`image_file` AS `image_file`,
        `item`.`category_no` AS `category_no`,
        `item`.`item_latitude` AS `item_latitude`,
        `item`.`item_longitude` AS `item_longitude`
    FROM
        (((`item`
        JOIN `seller` ON ((`item`.`seller_no` = `seller`.`seller_no`)))
        JOIN `user` ON ((`seller`.`user_no` = `user`.`user_no`)))
        JOIN `image` ON ((`item`.`item_no` = `image`.`item_no`)))
    WHERE
        ((`item`.`item_sale_type` = 'AUCTION')
            AND (`item`.`item_sale_start_date` < NOW())
            AND (`item`.`item_status` = 'ACTIVE'))
    GROUP BY `item`.`item_no`;
