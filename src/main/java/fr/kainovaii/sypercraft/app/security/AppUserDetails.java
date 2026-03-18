package fr.kainovaii.sypercraft.app.security;

import com.obsidian.core.security.user.UserDetails;

public interface AppUserDetails extends UserDetails
{
    int getStaffRank();
    int getVipRank();
    long getPlaytimeSeconds();
    String getUUID();
}