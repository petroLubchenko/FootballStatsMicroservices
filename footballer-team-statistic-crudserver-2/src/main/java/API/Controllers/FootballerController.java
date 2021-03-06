package API.Controllers;

import API.Additional.Result;
import API.Controllers.Exceptions.InternalServerException;
import API.Controllers.Exceptions.NotFoundException;
import API.Models.Footballer;
import API.Services.FootballerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static API.Controllers.ErrorHandlers.inadmissibleNullFieldHandler;


@RestController
@RequestMapping("/footballers")
public class FootballerController {
    @Autowired
    FootballerService footballerService;

    @GetMapping("/")
    public ResponseEntity<List<Footballer>> getAll(){
        System.out.println("Running \'getAll\' method from FootballerController");
        return new ResponseEntity<>(footballerService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id){
        System.out.println("Running \'delete\' method for id = \'" + id + "\' from FootballerController");
        Result result = footballerService.delete(id);

        if (result == Result.Not_Found)
            throw new NotFoundException(generateNotFoundMessage(id));
            //return notFoundHandler(generateNotFoundMessage(id));



        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity create(@RequestBody Footballer footballer){
        System.out.println("Running \'add\' method for id = \'" + footballer.getId() + "\' from FootballerController");
        if (footballer == null)
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Reason", "No entity in body");
            return new ResponseEntity(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (!footballer.isValid(false)){
            List<String> field = new ArrayList<>();

            if (footballer.getFirstname() == null)
                field.add("firstname");

            if (footballer.getSurname() == null)
                field.add("surname");

            if (footballer.getAge() <= 15)
                field.add("age");

            return inadmissibleNullFieldHandler(field, this.getClass());
        }

        boolean isSuccesful = footballerService.addFootballer(footballer);

        if (isSuccesful) {
            ResponseEntity re = new ResponseEntity(HttpStatus.CREATED);
            return re;
        }

        throw new InternalServerException("Unsuccessful operation. Check object and try to add again.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Footballer> getFootballer(@PathVariable long id){
        System.out.println("Running \'getById\' method from FootballerController");
        Footballer res = footballerService.getById(id);
        if (res != null)
            return new ResponseEntity<>(res, HttpStatus.OK);
        // TODO throw mistake
        throw new NotFoundException(generateNotFoundMessage(id));
        //return notFoundHandler(generateNotFoundMessage(id));
    }

    @PostMapping("/update")
    public ResponseEntity updateFootballer(@RequestBody Footballer footballer){
        System.out.println("Running \'update\' method for id = \'" + footballer.getId() + "\' from FootballerController");

        if (footballer == null)
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Reason", "No entity in body");
            return new ResponseEntity(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (!footballer.isValid(true)){
            List<String> field = new ArrayList<>();

            if (footballer.getId() == 0L)
                field.add("id");

            if (footballer.getFirstname() == null)
                field.add("firstname");

            if (footballer.getSurname() == null)
                field.add("surname");

            if (footballer.getAge() <= 15)
                field.add("age");

            return inadmissibleNullFieldHandler(field, this.getClass());
        }


        if (footballer.getId() == 0L)
        {
            throw new NotFoundException(generateNotFoundMessage(footballer.getId()));
            //return notFoundHandler(generateNotFoundMessage(footballer.getId()));
        }

        Result result = footballerService.update(footballer);

        switch (result){
            case False:
                throw new InternalServerException("Unsuccessful operation. Check object and try to add again.");
            case True:
                return new ResponseEntity(HttpStatus.OK);
            case Not_Found:
                throw new NotFoundException(generateNotFoundMessage(footballer.getId()));
        }

        throw new InternalServerException("Unexpected result");
    }

    private String generateNotFoundMessage(long id){
        return "Footballer with id = \'" + id + "\' does not exist";
    }


}
