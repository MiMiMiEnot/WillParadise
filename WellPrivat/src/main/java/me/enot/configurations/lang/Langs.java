package me.enot.configurations.lang;

public enum Langs {

    main__rgs_empty("main.load-rgs-empty"),
    main__load_x_rgs("main.load-x-rgs"),
    main__player_deleted_from_rg("main.player-deleted-from-rg"),
    rgs__max_privats("rgs.max-privats"),
    rgs__block_at_privat("rgs.block-at-privat"),
    rgs__chat_to_small("rgs.chat-to-small"),
    rgs__chat_to_big("rgs.chat-to-big"),
    rgs__privat_created("rgs.privat-created"),
    rgs__player_come_in_rg("rgs.player-come-in-rg"),
    rgs__player_come_out_rg("rgs.player-come-out-rg"),
    rgs__player_not_in_privat("rgs.player-not-in-privat"),
    rgs__privat_create_title("rgs.privat-create-title"),
    rgs__privat_create_subtitle("rgs.privat-create-subtitle"),
    rgs__player_not_owner_break_private_block("rgs.player-not-owner-break-private-block"),
    rgs__rg_deleted_succesfully("rgs.rg-deleted-succesfully"),
    rgs__rg_deleting_error("rgs.rg-deleting-error"),
    rgs__rg_deleting_no("rgs.rg-deleting-no"),
    rgs__privat_player_add_title("rgs.privat-player-add-title"),
    rgs__privat_player_add_subtitle("rgs.privat-player-add-subtitle"),
    rgs__privat_player_add_uncorrect("rgs.privat-player-add-uncorrect"),
    rgs__privat_player_use_alredy_in_privat("rgs.privat-player-use-alredy-in-privat"),
    rgs__privat_player_add("rgs.privat-player-add"),
    rgs__privat_name_contains_bed_chats("rgs.privat-name-contains-bed-chats"),
    main__net_prav("main.net-prav"),
    rgs__not_in_rg("rgs.not-in-rg"),
    rgs__net_rg_with_id("rgs.net-rg-with-id"),
    rgs__rg_command_use("rgs.rg-command-use"),
    rgs__rg_member_no("rgs.rg-member-no"),
    main__reload("main.reload"),
    rgs__not_rg_world("rgs.not-rg-world"),
    spawn__bed_destroyed_if_online("spawn.bed-destroyed-if-online"),
    spawn__bed_destroyed_if_offline("spawn.bed-destroyed-if-offline"),
    spawn__spawn_created("spawn.spawn-created");

    private String path;

    Langs(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
