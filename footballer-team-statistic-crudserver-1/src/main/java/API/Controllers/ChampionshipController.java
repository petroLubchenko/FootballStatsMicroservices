package API.Controllers;

import API.Controllers.Exceptions.InternalServerException;
import API.Controllers.Exceptions.NotFoundException;
import API.Models.Championship;
import API.Services.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static API.Controllers.ErrorHandlers.inadmissibleNullFieldHandler;

@RestController
@RequestMapping("/championships")
public class ChampionshipController {
    @Autowired
    ChampionshipService championshipService;

    @GetMapping("/")
    public ResponseEntity<List<Championship>> getAll(){
        return new ResponseEntity<>(championshipService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id){
        Boolean resuly = championshipService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Championship> getById(@PathVariable long id){
        Championship c = championshipService.getById(id);
        if (c != null)
            return new ResponseEntity<>(c, HttpStatus.OK);
        throw new NotFoundException("Championship with id = \'" + id + "\' was not found.");
    }

    @PostMapping("/add")
    public ResponseEntity create(@RequestBody Championship footballer){
        System.out.println("Running \'add\' method for id = \'" + footballer.getId() + "\' from FootballerController");
        if (footballer == null)
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Reason", "No entity in body");
            return new ResponseEntity(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (!footballer.isValid(false)){
            List<String> field = new ArrayList<>();

            if (footballer.getName() == null)
                field.add("firstname");

            if (footballer.getAbbreviatedname() == null)
                field.add("surname");

            return inadmissibleNullFieldHandler(field, this.getClass());
        }

        boolean isSuccesful = championshipService.addFootballer(footballer);

        if (isSuccesful) {
            ResponseEntity re = new ResponseEntity(HttpStatus.CREATED);
            return re;
        }

        throw new InternalServerException("Unsuccessful operation. Check object and try to add again.");
    }

    @PostMapping("/update")
    public ResponseEntity updateFootballer(@RequestBody Championship championship){
        System.out.println("Running \'update\' method for id = \'" + championship.getId() + "\' from FootballerController");

        if (championship == null)
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Reason", "No entity in body");
            return new ResponseEntity(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (!championship.isValid(true)){
            List<String> field = new ArrayList<>();

            if (championship.getId() == 0L)
                field.add("id");

            if (championship.getName() == null)
                field.add("firstname");

            if (championship.getAbbreviatedname() == null)
                field.add("surname");

            return inadmissibleNullFieldHandler(field, this.getClass());
        }


        if (championship.getId() == 0L)
        {
            throw new NotFoundException("Championship with id = \'" + championship.getId() + "\' was not found.");
        }

        boolean result = championshipService.update(championship);

        if (!result)
            throw new InternalServerException("Unsuccessful operation. Check object and try to add again.");

        throw new InternalServerException("Unexpected result");
    }

}
