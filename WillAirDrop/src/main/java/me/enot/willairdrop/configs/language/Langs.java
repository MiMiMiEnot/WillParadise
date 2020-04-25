package me.enot.willairdrop.configs.language;

public enum Langs {

    main__loots_loader,
    main__reload,
    main__hasnt_permissions,
    main__error__load_error_loot_invalid_time,
    crate__time_to_box,
    crate__time_to_box_admin,
    crate__console_name,
    crate__hours,
    crate__minutes,
    crate__seconds,
    crate__admin_loot_start,
    crate__admin_start_usage,
    crate__admin_time_not_write,
    crate__admin_loot_not_find,
    crate__admin_coords_not_write,
    crate__min_players,
    crate__air_drop_opened,
    crate__to_open_message,
    crate__air_drop_opening_cancelled,
    crate__airdrop_broadcat_cancelled_message,
    hologram__air_drop_counter,
    hologram__airdrop,
    hologram__airdrop_opened,
    hologram__airdrop_cancelled;


    public String convert(){
        String tostr = this.toString();
        String result = tostr.replaceAll("__", "\\.").replaceAll("_", "-");
        return result;
    }
}
