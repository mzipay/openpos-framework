import { JumpposCatalogPage } from './app.po';

describe('jumppos-catalog App', () => {
  let page: JumpposCatalogPage;

  beforeEach(() => {
    page = new JumpposCatalogPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
