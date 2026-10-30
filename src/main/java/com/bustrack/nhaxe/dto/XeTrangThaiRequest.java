package vn.busapp.nhaxe.dto;

import jakarta.validation.constraints.NotNull;
import vn.busapp.nhaxe.entity.XeTrangThai;

public record XeTrangThaiRequest(@NotNull XeTrangThai trangThai) {}
