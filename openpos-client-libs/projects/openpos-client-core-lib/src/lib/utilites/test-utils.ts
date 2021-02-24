import {By} from "@angular/platform-browser";

export const validateDoesNotExist = (fixture, selector: string) => {
    const button = fixture.debugElement.query(By.css(selector));
    expect(button).toBeNull();
};

export const validateText = (fixture, selector: string, value: string) => {
    const element = fixture.debugElement.query(By.css(selector));
    expect(element.nativeElement).toBeDefined();
    expect(element.nativeElement.textContent).toContain(value);
}

export const validateIcon = (fixture, selector: string, iconName: string) => {
    const iconElement = fixture.debugElement.query(By.css(selector));
    expect(iconElement.nativeElement).toBeDefined();
    expect(iconElement.properties['iconName']).toBe(iconName);
}