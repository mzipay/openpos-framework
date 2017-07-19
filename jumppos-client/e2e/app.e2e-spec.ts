import { OpenposPage } from './app.po';

describe('openpos App', () => {
  let page: OpenposPage;

  beforeEach(() => {
    page = new OpenposPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
