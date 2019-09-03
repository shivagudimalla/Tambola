package com.tambola.validator;

import com.tambola.components.Ticket;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameValidator.class)
public class GameValidatorTest {

    @Autowired
    private GameValidator gameValidator;

    @Mock
    private Ticket ticket;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<List<Integer>> testTicket = new ArrayList<>();
        testTicket.add(new ArrayList<>());
        List<Integer> ticketColumnsForRow0 = new ArrayList<>(Arrays.asList(1, 0, 7, 0, 9, 4));
        testTicket.get(0).addAll(ticketColumnsForRow0);
        testTicket.add(new ArrayList<>());
        List<Integer> ticketColumnsForRow1 = new ArrayList<>(Arrays.asList(11, 0, 17, 0, 19, 14));
        testTicket.get(1).addAll(ticketColumnsForRow1);
        testTicket.add(new ArrayList<>());
        List<Integer> ticketColumnsForRow2 = new ArrayList<>(Arrays.asList(21, 0, 27, 0, 29, 24));
        testTicket.get(2).addAll(ticketColumnsForRow2);
        when(ticket.getTicketNumbers()).thenReturn(testTicket);
        when(ticket.getItemsPerRow()).thenReturn(4);


    }

    @Test
    public void testCheckTopRowFalse() {
        List<Integer> announcedList = new ArrayList<>(Arrays.asList(11, 1, 7, 0, 9, 24));
        assertFalse(gameValidator.checkTopRowWinner(ticket, announcedList));
    }

    @Test
    public void testCheckTopRowTrue() {
        List<Integer> announcedList = new ArrayList<>(Arrays.asList(11, 1, 7, 9, 24, 4));
        assertTrue(gameValidator.checkTopRowWinner(ticket, announcedList));
    }

}
