# markmap-toolbar

Toolbar for [markmap](https://markmap.js.org/).

It embeds a few buttons to communicate with a markmap.

## Installation

```sh
$ npm i markmap-toolbar
```

## Usage

```js
import { Toolbar } from 'markmap-toolbar';

const el = Toolbar.create(mm);
el.style.position = 'absolute';
el.style.bottom = '0.5rem';
el.style.right = '0.5rem';
container.append(el);
```

```js
//Normal usage
    const {Toolbar}=window.markmap

    console.log(mm)
    const el = Toolbar.create(mm);
    el.setAttribute('class','mm-toolbar d-flex align-items-center')
    //document.getElementById(id).append()
    //console.log(Markmap)
    $(id).innerHTML=''
    $(id).append(el)
    
```