import { Component } from '@angular/core';

import { Category, Product, ProductService } from './products.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

// PrimeNG Modules
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TableLazyLoadEvent } from 'primeng/table';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';


@Component({
  selector: 'app-products',
  standalone: true,
  imports: [ToastModule, ConfirmDialogModule, CheckboxModule, FormsModule, ReactiveFormsModule, DialogModule, TagModule, CommonModule, TableModule, InputTextareaModule, InputNumberModule, ButtonModule, InputGroupModule, InputGroupAddonModule, InputTextModule, DropdownModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss',
  providers: [MessageService, ConfirmationService],
})
export class ProductsComponent {

  products: Product[] = [];
  categories: Category[] = [];
  totalRecords: number = 0;
  loading: boolean = false;
  rows: number = 5; 
  searchTerm: string = '';

  displayDialog: boolean = false;
  productForm: FormGroup;
  isEditMode: boolean = false;

  constructor(
    private productService: ProductService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, Validators.required],
      available: [false],
      category: [null] 
    });
  }

  ngOnInit() {
    this.loading = true;
    this.loadCategories();
  }

  loadCategories() {
    this.productService.loadCategories().subscribe(
      (data) => {
        this.categories = data;
      },
      (error) => {
        console.error('Error loading categories', error);
      }
    );
  }

  loadProductsLazy(event: TableLazyLoadEvent) {
    this.loading = true;
    const page = event.first! / event.rows!;
    const size = event.rows!;
    const search = this.searchTerm;

    this.productService.getProducts(page, size, search).subscribe(
      data => {
        this.products = data.content;
        this.totalRecords = data.page.totalElements;
        this.loading = false;
      },
      error => {
        console.error(error);
        this.loading = false;
      }
    );
  }

  onSearch(event: any) {
    this.searchTerm = event.target.value;
    this.loadProductsLazy({ first: 0, rows: this.rows });
  }

  onAdd() {
    this.productForm.reset();
    this.isEditMode = false;
    this.displayDialog = true;
  }

  onEdit(product: Product) {
    this.productForm.patchValue(product);
    this.isEditMode = true;
    this.displayDialog = true;
  }

  saveProduct() {
    if (this.productForm.invalid) {
      return;
    }

    const product: Product = this.productForm.value;

    if (this.isEditMode) {
      // Atualizar produto
      this.productService.updateProduct(product).subscribe(
        () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Sucsess',
            detail: 'Product updated successfully'
          });
          this.displayDialog = false;
          this.loadProductsLazy({ first: 0, rows: this.rows });
        },
        error => {
          console.error(error);
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: 'Error while updating product'
          });
        }
      );
    } else {
      // Criar novo produto
      this.productService.createProduct(product).subscribe(
        () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Product created successfully'
          });
          this.displayDialog = false;
          this.loadProductsLazy({ first: 0, rows: this.rows });
        },
        error => {
          console.error(error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error while creating product'
          });
        }
      );
    }
  }

  onDelete(product: Product) {
    this.confirmationService.confirm({
      message: `Are you sure you want to delete "${product.name}"?`,
      accept: () => {
        this.productService.deleteProduct(product.id).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Product deleted'
            });
            this.loadProductsLazy({ first: 0, rows: this.rows });
          },
          error => {
            console.error(error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error while deleting product'
            });
          }
        );
      }
    });
  }
}
