package API.Services;

import API.Additional.Result;
import API.Controllers.Exceptions.InternalServerException;
import API.Controllers.Exceptions.NotFoundException;
import API.Models.Team;
import API.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    public Team getById(long id){
        try {
            Optional<Team> optteam = teamRepository.findById(id);

            return optteam.isPresent() && !optteam.get().getDeleted() ? optteam.get() : null;
        }
        catch(Exception e)
        {
            throw new InternalServerException(e.getMessage());
        }
    }

    public List<Team> getAll(){
        //return (List<Team>)(teamRepository.findAll());

        List<Team> fs = (List<Team>)teamRepository.findAll();
        List<Team> res = new ArrayList<>();

        for (int i = 0; i < fs.size(); i++)
            if (!fs.get(i).getDeleted())
                res.add(fs.get(i));

        return res;

    }

    public Boolean addTeam(Team team){
        try {
            if (team == null)
                return false;

            if (teamRepository.existsById(team.getId()))
                return false;

            return teamRepository.save(team) != null;
        }
        catch (Exception e){
            throw new InternalServerException("Error during adding a new team to database. Please check data and retry.");
        }
    }

    public Result delete(long id){
        try {
            if (!teamRepository.existsById(id))
                return Result.Not_Found;

            /*teamRepository.deleteById(id);

            return teamRepository.existsById(id) ? Result.False : Result.True;*/

            Optional<Team> of = teamRepository.findById(id);
            Team f = of.get();

            if (f.getDeleted())
                return Result.Not_Found;

            f.setDeleted(true);

            teamRepository.save(f);

            if (teamRepository.findById(id).get().getDeleted())
                return Result.True;
            else
                return Result.False;

        }
        catch(Exception e){
            throw new InternalServerException("Object deleting unsuccessfull. Please try again.");
        }
    }

    public Result update(Team team){
        try {
            if (!teamRepository.existsById(team.getId()))
                throw new NotFoundException("Team with id = \'" + team.getId() + "\' does not exist.");

            Team t = teamRepository.save(team);

            return t == null ? Result.False : Result.True;
        }
        catch (Exception e){
            throw new InternalServerException("An error occured during adding team with id = \'" + team.getId() +
                    "\'. Please check data and try again.");
        }
    }
}
