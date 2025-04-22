CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `mp2`@`%` 
    SQL SECURITY DEFINER
VIEW `item_include_image_for_brand` AS
    SELECT 
        `i`.`item_no` AS `item_no`,
        `i`.`item_price` AS `item_price`,
        `i`.`item_quantity` AS `item_quantity`,
        `i`.`item_title` AS `item_title`,
        `i`.`item_view_cnt` AS `item_view_cnt`,
        `i`.`item_sale_start_date` AS `item_sale_start_date`,
        `i`.`item_sale_end_date` AS `item_sale_end_date`,
        `u`.`user_nick` AS `user_nick`,
        MIN(`img`.`image_file`) AS `image_file`,
        `i`.`category_no` AS `category_no`
    FROM
        (((`item` `i`
        JOIN `seller` `s` ON ((`i`.`seller_no` = `s`.`seller_no`)))
        JOIN `user` `u` ON ((`s`.`user_no` = `u`.`user_no`)))
        JOIN `image` `img` ON ((`i`.`item_no` = `img`.`item_no`)))
    WHERE
        ((`i`.`item_sale_type` = 'SHOP')
            AND (`i`.`item_sale_start_date` < NOW())
            AND (`i`.`item_status` = 'ACTIVE'))
    GROUP BY `i`.`item_no`;
