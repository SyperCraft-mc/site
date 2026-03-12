package fr.kainovaii.obsidian.app.security;

import fr.kainovaii.obsidian.security.user.UserDetails;

public interface AppUserDetails extends UserDetails
{
    int getStaffRank();
    int getVipRank();
    long getPlaytimeSeconds();
    String getUUID();
}