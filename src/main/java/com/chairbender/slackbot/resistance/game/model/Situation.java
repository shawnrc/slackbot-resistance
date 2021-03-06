package com.chairbender.slackbot.resistance.game.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * encapsulates the current situation of the game. i.e. players, roles, turns, etc...
 *
 * Created by chairbender on 11/18/2015.
 */
public class Situation {

    private List<PlayerCharacter> playerCharacters;
    private PlayerCharacter leader;
    private Set<PlayerCharacter> teamMembers;
    //starts from 1 because that's how the game does it. Sorry :-(.
    private int currentRound;
    private int missionSuccesses;

    /**
     *
     * @param playerCharacters players, their state, and the order they are sitting around the table.
     * @param leader the current leader
     * @param teamMembers the players on the currently selected team
     * @param currentRound the current round.
     * @param missionSuccesses number of mission successes
     */

    public Situation(List<PlayerCharacter> playerCharacters, PlayerCharacter leader, Set<PlayerCharacter> teamMembers, int currentRound, int missionSuccesses) {
        this.playerCharacters = playerCharacters;
        this.leader = leader;
        this.teamMembers = teamMembers;
        this.currentRound = currentRound;
        this.missionSuccesses = missionSuccesses;
    }



    public List<PlayerCharacter> getPlayerCharacters() {
        return playerCharacters;
    }

    /**
     *
     * @return the set of player characters that are spies
     */
    public Set<PlayerCharacter> getSpies() {
        Set<PlayerCharacter> spies = new HashSet<>();
        for (PlayerCharacter playerCharacter : playerCharacters) {
            if (playerCharacter.isSpy()) {
                spies.add(playerCharacter);
            }
        }

        return spies;
    }

    /**
     *
     * @return the player who is the current team leader.
     */
    public PlayerCharacter getLeader() {
        return leader;
    }

    /**
     *
     * @return the number of players that need to go on the current team based on the current round number
     *      and number of players. -1 if there's too many or too few players
     */
    public int getRequiredTeamSize() {
        return RulesUtil.getRequiredTeamSize(playerCharacters.size(),currentRound);
    }

    /**
     *
     * @param toGet player to find
     * @return the player character played by the given player
     */
    public PlayerCharacter getPlayerCharacter(Player toGet) {
        for (PlayerCharacter playerCharacter : playerCharacters) {
            if (playerCharacter.getPlayer().equals(toGet)) {
                return playerCharacter;
            }
        }

        return null;
    }

    /**
     *
     * @param currentTeam the players to put on the mission team
     */
    public void setCurrentTeam(Set<PlayerCharacter> currentTeam) {
        this.teamMembers = currentTeam;
    }

    /**
     * move the leader to be the next in the player list
     */
    public void advanceLeader() {
        int leaderIndex = playerCharacters.indexOf(leader);
        if (leaderIndex == playerCharacters.size() - 1) {
            leader = playerCharacters.get(0);
        } else {
            leader = playerCharacters.get(leaderIndex+1);
        }
    }

    /**
     *
     * @return number of missions that succeeded
     */
    public int getMissionSuccess() {
        return missionSuccesses;
    }

    /**
     *
     * @return number of missions that failed
     */
    public int getMissionFails() {
        return (currentRound - 1) - missionSuccesses;
    }

    /**
     * advance the turn counter and mark the mission as a success or failure
     * @param success whether the mission succeeded
     */
    public void completeMission(boolean success) {
        currentRound++;
        missionSuccesses += success ? 1 : 0;
    }

    public int getRoundNumber() {
        return currentRound;
    }
}
