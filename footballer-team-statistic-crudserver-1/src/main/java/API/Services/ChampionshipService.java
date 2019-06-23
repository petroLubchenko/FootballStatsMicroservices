package API.Services;

import API.Controllers.Exceptions.InternalServerException;
import API.Controllers.Exceptions.NotFoundException;
import API.Models.Championship;
import API.Models.Footballer;
import API.Repository.ChampionshionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChampionshipService {
    private final ChampionshionshipRepository championshionshipRepository;

    @Autowired
    public ChampionshipService(ChampionshionshipRepository championshionshipRepository){
        this.championshionshipRepository = championshionshipRepository;
    }

    public Championship getById(long id){
        Optional<Championship> optionalChampionship = championshionshipRepository.findById(id);

        return optionalChampionship.orElse(null);
    }

    public List<Championship> getAll(){
        return (List<Championship>)championshionshipRepository.findAll();
    }

    public boolean addFootballer(Championship championship){
        try{
            if (championship == null)
                return false;
            if (championshionshipRepository.existsById(championship.getId()))
                return false;
            Championship c = championshionshipRepository.save(championship);
            return c != null;
        }
        catch (Exception e){
            throw new InternalServerException("Unsuccessful operation. Please check data and try again.");
        }
    }

    public boolean delete(long id){
        Optional<Championship> optionalChampionship = championshionshipRepository.findById(id);
        if (optionalChampionship.isPresent())
            throw new NotFoundException("Championship " + id + " does not exist.");
        championshionshipRepository.deleteById(id);
        return true;
    }

    public boolean update(Championship championship){
        try{
            if (!championshionshipRepository.existsById(championship.getId()))
                throw new NotFoundException("Object with id = \'" + championship.getId() + "\' does not exist.");
            Championship c = championshionshipRepository.save(championship);
            return true;
        } catch (Exception e){
            throw new InternalServerException("An error occured during adding championship with id = \\'" + championship.getId()
                    + "\\'. Please check data and try again.");
        }
    }

}
