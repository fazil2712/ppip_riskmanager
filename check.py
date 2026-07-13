from bs4 import BeautifulSoup
with open('src/main/resources/templates/report.html', 'r', encoding='utf-8') as f:
    soup = BeautifulSoup(f, 'html.parser')
main = soup.find('div', class_='main-content')
for child in main.children:
    if child.name == 'div':
        if child.has_attr('class') and 'card' in child['class']:
            h3 = child.find('h3')
            print('Card:', h3.text.strip().replace('
', ' ') if h3 else 'No H3')
            for sub in child.children:
                if sub.name == 'div' and sub.has_attr('class') and 'card' in sub['class']:
                    h3sub = sub.find('h3')
                    print('  --- NESTED Card:', h3sub.text.strip().replace('
', ' ') if h3sub else 'No H3')

