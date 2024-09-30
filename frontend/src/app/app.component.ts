import { Component } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { Aura } from 'primeng/themes/aura';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  
  title = 'ecommerce';
  visible: boolean = false;


  constructor(private config: PrimeNGConfig) {
    this.config.theme.set({ preset: Aura });
  }

  showDialog() {
    this.visible = true;
  }

}
