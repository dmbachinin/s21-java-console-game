package edu.school21.game.Args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;


@Parameters(separators = "=")
public class Args {
    @Parameter(names = {"--enemiesCount"}, required = true)
    private Integer enemiesCount;

    @Parameter(names = {"--wallsCount"}, required = true)
    private Integer wallsCount;

    @Parameter(names = {"--size"}, required = true)
    private Integer size;

    @Parameter(names = {"--profile"}, validateWith = ProfileValidator.class, required = false)
    private String profile;

    public Integer getEnemiesCount() {
        return enemiesCount;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getWallsCount() {
        return wallsCount;
    }

    public String getProfile() {
        return profile;
    }
}
