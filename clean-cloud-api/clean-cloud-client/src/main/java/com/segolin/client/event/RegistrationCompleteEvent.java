package com.segolin.client.event;

import com.segolin.client.entity.Employee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private Employee employee;
    private String applicationUrl;

    public RegistrationCompleteEvent(Employee employee, String applicationUrl) {
        super(employee);
        this.employee = employee;
        this.applicationUrl = applicationUrl;
    }

}
