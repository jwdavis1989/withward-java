package com.withward.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

 
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
 
import org.junit.Before;
import org.junit.Test;

//import com.withward.DTO.WithlistDTO;
//import com.withward.model.Destination;
import com.withward.model.Withlist;
import com.withward.repository.DestinationDAO;
import com.withward.repository.WithlistDAO;
import com.withward.service.WithlistService;

public class WithlistServletTest {
	
	private WithlistDAO mockedWLDAO;
	private DestinationDAO mockedDestDAO;
//	private Destination d;
	private Withlist w;
	private ArrayList<Withlist> withlists = new ArrayList<>();
//	private ArrayList<Destination> destinations = new ArrayList<>();
    
    @Before
    public void setUp() throws Exception {
        mockedWLDAO = mock(WithlistDAO.class);
        mockedDestDAO = mock(DestinationDAO.class);
//        d = new Destination(1, 1, "South Beach", "A beach.", "placeholder", false, 0.0f);
        w = new Withlist(1,1,"BEACHES","A list of beaches");
    }
    
    @Test
    public void getAllWithlistsEmptyWithlistTest() throws IOException, ServletException, SQLException {
    	when(mockedWLDAO.getAll(1)).thenReturn(new ArrayList<Withlist>());
    	
    	WithlistService wlService = new WithlistService(mockedWLDAO, mockedDestDAO);
    	assertEquals(new ArrayList<Withlist>(), wlService.getAllWithlists(1));
    }
    
    @Test
    public void searchByIdTest() throws IOException, ServletException, SQLException {
    	withlists.add(w);
    	when(mockedWLDAO.getAll(1)).thenReturn(withlists);
    	WithlistService wlService = new WithlistService(mockedWLDAO, mockedDestDAO);
    	assertEquals(withlists, wlService.getAllWithlists(1));
    }
    
//    @Test
//    public void getOneWithListWithAllDestinationsTest() throws IOException, ServletException, SQLException {
//    	destinations.add(d);
//        withlistDTO = new WithlistDTO(1,1,"BEACHES","A list of beaches", destinations);
//    	when(mockedWLDAO.getWithlist(1)).thenReturn(w);
//    	when(mockedDestDAO.getAll(1)).thenReturn(destinations);
//    	
//    	WithlistService wlService = new WithlistService(mockedWLDAO, mockedDestDAO);
//    	assertEquals(withlistDTO, wlService.getOneWithlist(1));
//    }
    
}
